package com.nevaDev.padeliummarhaba.di

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Looper
import android.widget.Toast
class NetworkUtil(private val context: Context) {
    private var isConnected = checkInternetConnection()
    private var onNetworkChange: ((Boolean) -> Unit)? = null

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val newConnectionStatus = checkInternetConnection()
            if (newConnectionStatus != isConnected) {
                onNetworkChange?.invoke(newConnectionStatus)
                isConnected = newConnectionStatus
                if (!newConnectionStatus) {
                    showToast(context, "No internet connection")
                }
            }
        }
    }

    init {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    fun registerNetworkCallback(callback: (Boolean) -> Unit) {
        onNetworkChange = callback
        callback(isConnected)
    }

    fun unregister() {
        context.unregisterReceiver(networkReceiver)
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun showToast(context: Context?, message: String) {
        if (context != null && Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
