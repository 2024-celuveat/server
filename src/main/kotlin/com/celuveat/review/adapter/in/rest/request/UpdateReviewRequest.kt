package com.celuveat.review.adapter.`in`.rest.request

import com.celuveat.review.application.port.`in`.command.UpdateReviewCommand
import com.celuveat.review.domain.Star
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateReviewRequest(
    @Schema(description = "리뷰 내용", example = "맛있어요")
    var content: String,
    @Schema(description = "리뷰 별점", example = "THREE")
    var star: Star,
    @Schema(description = "리뷰 이미지들", example = "[\"imgUrl1\", \"imgUrl2\"]")
    var images: List<String>,
) {
    fun toCommand(
        reviewId: Long,
        memberId: Long,
    ): UpdateReviewCommand {
        return UpdateReviewCommand(
            memberId = memberId,
            reviewId = reviewId,
            content = content,
            star = star,
            images = images,
        )
    }
}