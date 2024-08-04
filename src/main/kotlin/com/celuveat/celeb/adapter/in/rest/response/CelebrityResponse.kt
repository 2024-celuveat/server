package com.celuveat.celeb.adapter.`in`.rest.response

import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.`in`.result.YoutubeContentResult
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
        description = "컨텐츠 채널 ID",
        example = "@sunghikyung",
    )
    val channelId: String,
    @Schema(
        description = "컨텐츠 채널 URL",
        example = "https://www.youtube.com/@sungsikyung",
    )
    val channelUrl: String,
    @Schema(
        description = "컨텐츠 채널 이름",
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
        fun from(youtubeContent: YoutubeContentResult): YoutubeContentResponse {
            return YoutubeContentResponse(
                id = youtubeContent.id,
                channelId = youtubeContent.channelId,
                channelUrl = youtubeContent.channelUrl,
                channelName = youtubeContent.channelName,
                contentsName = youtubeContent.contentsName,
                restaurantCount = youtubeContent.restaurantCount,
                subscriberCount = youtubeContent.subscriberCount,
            )
        }
    }
}

data class SimpleCelebrityResponse(
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
        fun from(celebrity: CelebrityResult): SimpleCelebrityResponse {
            return SimpleCelebrityResponse(
                name = celebrity.name,
                profileImageUrl = celebrity.profileImageUrl,
            )
        }
    }
}
