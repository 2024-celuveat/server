package com.celuveat.restaurant.domain

data class RestaurantImage(
    val id: Long = 0,
    val name: String,
    val author: String,
    val url: String,
    val isThumbnail: Boolean,
)
