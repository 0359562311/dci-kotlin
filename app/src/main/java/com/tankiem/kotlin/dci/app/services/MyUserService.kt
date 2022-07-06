package com.tankiem.kotlin.dci.app.services

import com.tankiem.kotlin.dci.app.network.ResponseObject
import com.tankiem.kotlin.dci.app.network.responses.Student
import retrofit2.Response
import retrofit2.http.GET

interface MyUserService {
    @GET("user/me")
    suspend fun getMyProfile(): Response<ResponseObject<Student>>
}