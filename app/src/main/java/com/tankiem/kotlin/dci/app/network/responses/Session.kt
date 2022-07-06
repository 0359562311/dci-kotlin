package com.tankiem.kotlin.dci.app.network.responses

import com.google.gson.annotations.SerializedName

data class Session (
    @SerializedName("refresh")
    val refresh: String,
    @SerializedName("access")
    var access: String,
)