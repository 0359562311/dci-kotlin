package com.tankiem.kotlin.dci.app.services

import com.tankiem.kotlin.dci.app.network.ResponseObject
import com.tankiem.kotlin.dci.app.network.requests.LoginRequest
import com.tankiem.kotlin.dci.app.network.responses.Session
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface AuthenticationService {
    @POST("auth/jwt/create/")
    suspend fun login(@Body request: LoginRequest): Response<ResponseObject<Session>>

    @POST("auth/jwt/refresh")
    fun refreshToken(@Field("refresh") refresh: String): Call<ResponseObject<Session>>
}