package com.celuveat.celeb.application.port.`in`.result

data class CelebrityResult(
    val id: Long = 0,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,
    val restaurantCount: Int,
    val youtubeChannelResults: List<YoutubeChannelResult>,
)

data class YoutubeChannelResult(
    val channelId: String,
    val channelUrl: String,
    val contentsName: String,
    val subscriberCount: Long,
)
