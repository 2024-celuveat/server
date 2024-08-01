package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity

data class CelebrityResult(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,
    val youtubeChannelResults: List<YoutubeChannelResult>,
) {
    companion object {
        fun from(celebrity: Celebrity): CelebrityResult {
            return CelebrityResult(
                id = celebrity.id,
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
                introduction = celebrity.introduction,
                youtubeChannelResults = celebrity.youtubeChannels.map { YoutubeChannelResult.from(it) },
            )
        }
    }
}

data class SimpleCelebrityResult(
    val name: String,
    val profileImageUrl: String,
)
