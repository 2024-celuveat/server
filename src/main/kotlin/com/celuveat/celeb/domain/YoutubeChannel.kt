package com.celuveat.celeb.domain

data class YoutubeChannel(
    val id: Long = 0,
    val channelId: ChannelId,
    val channelUrl: String,
    val channelName: String,
    val contentsName: String,
    val contentsIntroduction: String,
    val restaurantCount: Int,
    val subscriberCount: Long,
)
