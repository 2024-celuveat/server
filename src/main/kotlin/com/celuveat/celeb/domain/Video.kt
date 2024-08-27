package com.celuveat.celeb.domain

import java.time.LocalDate

data class Video(
    val id: Long,
    val videoUrl: String,
    val uploadDate: LocalDate,
    val youtubeContent: YoutubeContent,
)
