package com.tankiem.kotlin.dci.app.network
import com.google.gson.annotations.SerializedName

data class ResponseObject <T> (
    @SerializedName("data")
    var data: T? = null,
    val version: String
)
