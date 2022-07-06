package com.tankiem.kotlin.dci.app.repository

import com.tankiem.kotlin.dci.utils.NetworkUtils
import com.tankiem.kotlin.dci.utils.Result
import com.tankiem.kotlin.dci.app.services.MyUserService
import org.json.JSONObject
import java.io.IOException

class UserRepository {
    private val myUserService = NetworkUtils.buildService(MyUserService::class.java)

    suspend fun getMyProfile() : Result {
        return try {
            val response = myUserService.getMyProfile()
            return if(response.isSuccessful) {
                Result.Success(data = response.body()?.data!!)
            } else {
                val jsonObject = JSONObject(response.errorBody()?.string()?:"")
                Result.Failure(message = jsonObject.getJSONObject("data").getString("detail"))
            }
        } catch (e: IOException) {
            Result.Failure(message = e.message?:"Unknown error")
        }
    }
}