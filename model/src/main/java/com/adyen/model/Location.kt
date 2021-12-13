package com.adyen.model

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun none(): Location = Location(-1.0, -1.0)
    }
}