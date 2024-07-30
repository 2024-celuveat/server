package com.celuveat.celeb.domain

data class YoutubeChannel(
    val channelId: String,
    val channelUrl: String,
    val contentsName: String,
    val contentsIntroduction: String,
    val subscriberCount: Long,
)
