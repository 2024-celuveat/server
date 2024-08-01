package com.celuveat.restaurant.domain

data class Restaurant(
    val id: Long = 0,
    val name: String,
    val category: String,
    val roadAddress: String,
    val phoneNumber: String?,
    val naverMapUrl: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val images: List<RestaurantImage>,
)
