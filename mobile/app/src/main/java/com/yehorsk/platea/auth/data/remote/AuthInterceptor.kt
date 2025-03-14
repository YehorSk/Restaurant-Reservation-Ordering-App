package com.yehorsk.platea.auth.data.remote

import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(
    private val prefs: MainPreferencesRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking { prefs.getToken() }
        val lang = runBlocking { prefs.getLang() }

        Timber.d("Token: $token")
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("Content-Type", "application/vnd.api+json")
        requestBuilder.header("Accept", "application/vnd.api+json")
        requestBuilder.addHeader("lang", "$lang")
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}