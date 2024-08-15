package com.celuveat.celeb.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

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

data class BestCelebrityResult(
    val celebrity: SimpleCelebrityResult,
    val restaurants: List<RestaurantPreviewResult>,
)

data class CelebrityWithInterestedResult(
    val celebrity: CelebrityResult,
    val interested: Boolean,
) {
    companion object {
        fun of(
            celebrity: Celebrity,
            isInterested: Boolean,
        ): CelebrityWithInterestedResult {
            return CelebrityWithInterestedResult(
                celebrity = CelebrityResult.from(celebrity),
                interested = isInterested,
            )
        }
    }
}
