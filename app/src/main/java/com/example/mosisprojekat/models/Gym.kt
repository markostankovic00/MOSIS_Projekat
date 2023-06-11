package com.example.mosisprojekat.models

data class Gym(
    val userId: String = "",
    val name: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val reviews: List<Review> = emptyList(),
    val rating: Double = 0.0,
    val documentId: String = ""
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}
