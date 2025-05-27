package com.nevaDev.padeliummarhaba.di

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
class JSessionInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {

    companion object {
        private const val COOKIE_KEY = "JSESSIONID"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val jsessionId = sharedPreferences.getString(COOKIE_KEY, null)

        val modifiedRequest = if (!jsessionId.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Cookie", "JSESSIONID=$jsessionId")
                .build()
        } else {
            request
        }

        val response = chain.proceed(modifiedRequest)

        response.headers("Set-Cookie").forEach { cookie ->
            if (cookie.startsWith("JSESSIONID=")) {
                val sessionId = cookie.substringAfter("JSESSIONID=").substringBefore(";")
                sharedPreferences.edit().putString(COOKIE_KEY, sessionId).apply()
            }
        }

        return response
    }
}
