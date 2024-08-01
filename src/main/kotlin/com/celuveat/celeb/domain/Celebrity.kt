package com.celuveat.celeb.domain

data class Celebrity(
    val id: Long = 0,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,
    val youtubeChannels: List<YoutubeChannel>,
)
