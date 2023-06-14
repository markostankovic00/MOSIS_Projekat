package com.example.mosisprojekat.models

data class UserData(
    val userId: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val points: Int = 0,
    val photoUrl: String = ""
)
