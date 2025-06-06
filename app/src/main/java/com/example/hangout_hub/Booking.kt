package com.example.hangout_hub


data class Booking(
    val bookingId: String = "", // Optional
    val restaurantId: String = "",
    val userId: String = "",
    val numberOfPeople: Int = 1,
    val date: String = "",
    val time: String = ""
)
