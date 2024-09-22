package com.celuveat.region.domain

data class Region(
    val id: Long = 0,
    val name: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
