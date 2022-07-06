package com.tankiem.kotlin.dci.app.network.responses

data class Student(
    val studentId: String,
    val id: Int,
    val name: String,
    val dob: String,
    val address: String?,
    val avatar: String?,
    val cover: String?,
    val gender: String?,
    val role: String,
    val phoneNumber: String?,
    val createAt: String?,
    val updateAt: String?,
    val gpa: Double,
    val administrativeClass: AdministrativeClass,
)
