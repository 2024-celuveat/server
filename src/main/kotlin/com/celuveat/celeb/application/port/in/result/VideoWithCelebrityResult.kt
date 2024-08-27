package com.celuveat.celeb.application.port.`in`.result

data class VideoWithCelebrityResult(
    val id: Long,
    val videoUrl: String,
    val celebrities: List<CelebrityWithSubscriberCountResult>,
)
