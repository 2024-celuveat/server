package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity

data class CelebrityResult(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,
    val youtubeContentResults: List<YoutubeContentResult>,
) {
    companion object {
        fun from(celebrity: Celebrity): CelebrityResult {
            return CelebrityResult(
                id = celebrity.id,
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
                introduction = celebrity.introduction,
                youtubeContentResults = celebrity.youtubeContents.map { YoutubeContentResult.from(it) },
            )
        }
    }
}

data class SimpleCelebrityResult(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
) {
    companion object {
        fun from(celebrity: Celebrity): SimpleCelebrityResult {
            return SimpleCelebrityResult(
                id = celebrity.id,
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
            )
        }
    }
}
