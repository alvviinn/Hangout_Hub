package com.example.hangout_hub

data class Restaurant(
    var id: String = "",
    val name: String = "",
    val location: String = "",
    val contact: String = "",
    val isVerified: Boolean = false,
    val imageUrl: String = ""
)