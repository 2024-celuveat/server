package com.celuveat.celeb.domain

import com.celuveat.restaurant.domain.Restaurant
import java.time.LocalDate

data class Video(
    val id: Long,
    val videoUrl: String,
    val uploadDate: LocalDate,
    val youtubeContent: YoutubeContent,
    val restaurants: List<Restaurant>,
)
