package com.tankiem.kotlin.dci.app.repository
import com.tankiem.kotlin.dci.app.network.requests.LoginRequest
import com.tankiem.kotlin.dci.app.services.AuthenticationService
import com.tankiem.kotlin.dci.utils.NetworkUtils
import com.tankiem.kotlin.dci.utils.Result
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class AuthenticationRepository {
    private val sessionService = NetworkUtils.buildService(AuthenticationService::class.java)

    suspend fun login(username: String, password: String): Result{
        return try {
            val response = sessionService.login(LoginRequest(username, password))
            return if(response.isSuccessful) {
                Result.Success(data = response.body()?.data!!)
            } else {
                val jsonObject = JSONObject(response.errorBody()?.string()?:"")
                Result.Failure(message = jsonObject.getJSONObject("data").getString("detail"))
            }
        } catch(e: SocketTimeoutException) {
            Result.Failure(message = "Timeout")
        }
        catch (e: IOException) {
            print(e.cause)
            Result.Failure(message = e.message?:"Unknown error")
        }
    }
}