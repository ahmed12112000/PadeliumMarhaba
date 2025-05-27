package com.nevaDev.padeliummarhaba.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isLoggedInFlow = MutableStateFlow(false)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    init {
        checkAndHandleSessionValidity()
    }

    fun saveAuthToken(token: String, expiresIn: Long) {
        val expiryTime = System.currentTimeMillis() + expiresIn
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putLong(KEY_EXPIRY_TIME, expiryTime)
            .apply()
        updateLoginState(true)
    }

    fun getAuthToken(): String? {
        return if (isSessionValid()) prefs.getString(KEY_AUTH_TOKEN, null) else null
    }

    fun isLoggedIn(): Boolean {
        return isSessionValid()
    }

    fun isSessionValid(): Boolean {
        val expiryTime = prefs.getLong(KEY_EXPIRY_TIME, 0)
        return expiryTime > System.currentTimeMillis() && !prefs.getString(KEY_AUTH_TOKEN, null).isNullOrEmpty()
    }

    private fun checkAndHandleSessionValidity() {
        updateLoginState(isSessionValid())
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).remove(KEY_EXPIRY_TIME).apply()
        updateLoginState(false)
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        _isLoggedInFlow.value = isLoggedIn
    }

    fun logout() {
        clearAuthToken()
    }

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_EXPIRY_TIME = "auth_expiry"
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun invalidateToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }
}
