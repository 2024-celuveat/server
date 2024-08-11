package com.celuveat.review.adapter.`in`.rest.request

import com.celuveat.review.application.port.`in`.command.WriteReviewCommand
import com.celuveat.review.domain.Star
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class WriteReviewRequest(
    @Schema(description = "음식점 ID", example = "1")
    @NotNull val restaurantId: Long,
    @Schema(description = "리뷰 내용", example = "맛있어요")
    @NotBlank var content: String,
    @Schema(description = "리뷰 별점", example = "THREE")
    @NotNull var star: Star,
    @Schema(description = "리뷰 이미지들", example = "[\"imgUrl1\", \"imgUrl2\"]")
    var images: List<String>,
) {
    fun toCommand(memberId: Long): WriteReviewCommand {
        return WriteReviewCommand(
            memberId = memberId,
            restaurantId = restaurantId,
            content = content,
            star = star,
            images = images,
        )
    }
}
