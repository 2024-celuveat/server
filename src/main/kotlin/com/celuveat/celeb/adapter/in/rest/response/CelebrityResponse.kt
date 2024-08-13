package com.celuveat.celeb.adapter.`in`.rest.response

import com.celuveat.celeb.application.port.`in`.result.BestCelebrityResult
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.`in`.result.SimpleCelebrityResult
import com.celuveat.celeb.application.port.`in`.result.YoutubeContentResult
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import io.swagger.v3.oas.annotations.media.Schema

data class CelebrityResponse(
    @Schema(
        description = "연예인 ID",
        example = "1",
    )
    val id: Long,
    @Schema(
        description = "연예인 이름",
        example = "성시경",
    )
    val name: String,
    @Schema(
        description = "프로필 이미지 URL",
        example = "https://example.com/profile.jpg",
    )
    val profileImageUrl: String,
    @Schema(
        description = "소개",
        example = "안녕하세요. 성시경입니다.",
    )
    val introduction: String,
    @Schema(
        description = "유튜브 컨텐츠 목록",
    )
    val youtubeContentResults: List<YoutubeContentResponse>,
) {
    companion object {
        fun from(celebrity: CelebrityResult): CelebrityResponse {
            return CelebrityResponse(
                id = celebrity.id,
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
                introduction = celebrity.introduction,
                youtubeContentResults = celebrity.youtubeContentResults.map { YoutubeContentResponse.from(it) },
            )
        }
    }
}

data class YoutubeContentResponse(
    @Schema(
        description = "유튜브 컨텐츠 ID",
        example = "1",
    )
    val id: Long,
    @Schema(
        description = "컨텐츠 이름",
        example = "먹을텐데",
    )
    val contentsName: String,
    @Schema(
        description = "유튜브 채널 ID",
        example = "@sunghikyung",
    )
    val channelId: String,
    @Schema(
        description = "유튜브 채널 URL",
        example = "https://www.youtube.com/@sungsikyung",
    )
    val channelUrl: String,
    @Schema(
        description = "유튜브 채널 이름",
        example = "성시경 SUNG SI KYUNG",
    )
    val channelName: String,
    @Schema(
        description = "식당 수",
        example = "10",
    )
    val restaurantCount: Int,
    @Schema(
        description = "구독자 수",
        example = "100000",
    )
    val subscriberCount: Long,
) {
    companion object {
        fun from(result: YoutubeContentResult): YoutubeContentResponse {
            return YoutubeContentResponse(
                id = result.id,
                channelId = result.channelId,
                channelUrl = result.channelUrl,
                channelName = result.channelName,
                contentsName = result.contentsName,
                restaurantCount = result.restaurantCount,
                subscriberCount = result.subscriberCount,
            )
        }
    }
}

data class SimpleCelebrityResponse(
    @Schema(
        description = "연예인 ID",
        example = "1",
    )
    val id: Long,
    @Schema(
        description = "연예인 이름",
        example = "성시경",
    )
    val name: String,
    @Schema(
        description = "프로필 이미지 URL",
        example = "https://example.com/profile.jpg",
    )
    val profileImageUrl: String,
) {
    companion object {
        fun from(result: CelebrityResult): SimpleCelebrityResponse {
            return SimpleCelebrityResponse(
                id = result.id,
                name = result.name,
                profileImageUrl = result.profileImageUrl,
            )
        }

        fun from(result: SimpleCelebrityResult): SimpleCelebrityResponse {
            return SimpleCelebrityResponse(
                id = result.id,
                name = result.name,
                profileImageUrl = result.profileImageUrl,
            )
        }
    }
}

data class BestCelebrityResponse(
    @Schema(
        description = "연예인 정보",
    )
    val celebrity: SimpleCelebrityResponse,
    @Schema(
        description = "식당 정보",
    )
    val restaurants: List<RestaurantPreviewResponse>,
) {
    companion object {
        fun from(result: BestCelebrityResult): BestCelebrityResponse {
            return BestCelebrityResponse(
                celebrity = SimpleCelebrityResponse.from(result.celebrity),
                restaurants = result.restaurants.map { RestaurantPreviewResponse.from(it) },
            )
        }
    }
}
