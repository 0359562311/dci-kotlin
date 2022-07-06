package com.tankiem.kotlin.dci.utils

import com.tankiem.kotlin.dci.app.services.AuthenticationService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


sealed class Result  {
    data class Success<D>(val data: D) : Result()
    data class Failure(val message: String) : Result()
}

object AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(request.url.host.contains("keycloak").not() || request.url.host.contains("keycloak")) GlobalVariable.session?.let {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer ${it.access}")
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}

object ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 401
            && (request.url.host.contains("keycloak").not() || request.url.host.contains("keycloak"))
            && GlobalVariable.session != null
        ) {
            val sessionService = NetworkUtils.buildService(AuthenticationService::class.java)
            val newResponse = sessionService.refreshToken(GlobalVariable.session!!.refresh).execute()
            if (newResponse.code() == 400) {
                GlobalVariable.session = null
                SharePreferenceUtils.removeKey("access")
                SharePreferenceUtils.removeKey("refresh")
                GlobalVariable.currentUser = null
                return response
            } else {
                newResponse.body()?.data?.let {
                    GlobalVariable.session = it
                    SharePreferenceUtils.addString("access", it.access)
                    SharePreferenceUtils.addString("refresh", it.refresh)
                    response.close()
                    val newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer ${it.access}")
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }
        return response
    }

}

object NetworkUtils {
    private const val BASE_URL = Const.BASE_URL
    private val retrofit: Retrofit

    init {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(AuthenticationInterceptor)
            .addInterceptor(ErrorInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}