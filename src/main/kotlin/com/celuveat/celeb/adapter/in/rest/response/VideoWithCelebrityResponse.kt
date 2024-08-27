package com.celuveat.celeb.adapter.`in`.rest.response

import com.celuveat.celeb.application.port.`in`.result.VideoWithCelebrityResult
import io.swagger.v3.oas.annotations.media.Schema

data class VideoWithCelebrityResponse(
    @Schema(
        description = "영상 ID",
        example = "1",
    )
    val id: Long,
    @Schema(
        description = "영상 URL",
        example = "https://example.com/video.mp4",
    )
    val videoUrl: String,
    @Schema(
        description = "연예인 목록",
    )
    val celebrities: List<CelebrityWithSubscriberCountResponse>,
) {
    companion object {
        fun from(result: VideoWithCelebrityResult): VideoWithCelebrityResponse {
            return VideoWithCelebrityResponse(
                id = result.id,
                videoUrl = result.videoUrl,
                celebrities = result.celebrities.map { CelebrityWithSubscriberCountResponse.from(it) },
            )
        }
    }
}
