package com.example.hangout_hub

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val contact: String = "",
    val isVerified: Boolean = false,
    val imageUrl: String = ""
)