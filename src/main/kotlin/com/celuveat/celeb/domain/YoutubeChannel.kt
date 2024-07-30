package com.celuveat.celeb.domain

data class YoutubeChannel(
    val id: Long = 0,
    val channelId: String,
    val channelUrl: String,
    val contentsName: String,
    val contentsIntroduction: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
)
