package com.tankiem.kotlin.dci.app.network.responses

data class AdministrativeClass(
    val id: Int,
    val administrativeClassId: String,
    val faculty: String,
    val lecturer: Int
)