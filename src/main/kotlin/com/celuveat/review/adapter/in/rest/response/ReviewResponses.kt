package com.celuveat.review.adapter.`in`.rest.response

import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import com.celuveat.review.application.port.`in`.result.ReviewWriterInfo
import com.celuveat.review.application.port.`in`.result.SingleReviewResult
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ReviewPreviewResponse(
    @Schema(description = "리뷰 ID", example = "1")
    val id: Long,
    @Schema(description = "음식점 ID", example = "1")
    val restaurantId: Long,
    @Schema(description = "작성자 정보")
    val writer: ReviewWriterInfoResponse,
    @Schema(description = "리뷰 내용", example = "맛나요.")
    var content: String,
    @Schema(description = "리뷰 별점", example = "3")
    var star: Int,
    @Schema(description = "리뷰 조회수", example = "3")
    var views: Long,
    @Schema(description = "리뷰 도움돼요 수", example = "3")
    var helps: Long,
    @Schema(description = "리뷰 도움돼요 클릭 여부", example = "true")
    val clickedHelpful: Boolean,
    @Schema(description = "리뷰 이미지 url 들", example = "[\"imgUrl1\", \"imgUrl2\"]")
    var images: List<String>,
    @Schema(description = "리뷰 작성일")
    val createdAt: LocalDateTime,
    @Schema(description = "리뷰 수정일")
    var updatedAt: LocalDateTime,
) {
    companion object {
        fun from(review: ReviewPreviewResult): ReviewPreviewResponse {
            return ReviewPreviewResponse(
                id = review.id,
                restaurantId = review.restaurantId,
                writer = ReviewWriterInfoResponse.from(review.writer),
                content = review.content,
                star = review.star.score,
                views = review.views,
                helps = review.helps,
                clickedHelpful = review.clickedHelpful,
                images = review.images,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt,
            )
        }
    }
}

data class SingleReviewResponse(
    @Schema(description = "리뷰 ID", example = "1")
    val id: Long,
    @Schema(description = "음식점 ID", example = "1")
    val restaurantId: Long,
    @Schema(description = "작성자 정보")
    val writer: ReviewWriterInfoResponse,
    @Schema(description = "리뷰 내용", example = "맛나요.")
    var content: String,
    @Schema(description = "리뷰 별점", example = "3")
    var star: Int,
    @Schema(description = "리뷰 조회수", example = "3")
    var views: Long,
    @Schema(description = "리뷰 도움돼요 수", example = "3")
    var helps: Long,
    @Schema(description = "리뷰 도움돼요 클릭 여부", example = "true")
    val clickedHelpful: Boolean,
    @Schema(description = "리뷰 이미지들", example = "[\"imgUrl1\", \"imgUrl2\"]")
    var images: List<String>,
    @Schema(description = "리뷰 작성일")
    val createdAt: LocalDateTime,
    @Schema(description = "리뷰 수정일")
    var updatedAt: LocalDateTime,
) {
    companion object {
        fun from(review: SingleReviewResult): SingleReviewResponse {
            return SingleReviewResponse(
                id = review.id,
                restaurantId = review.restaurantId,
                writer = ReviewWriterInfoResponse.from(review.writer),
                content = review.content,
                star = review.star.score,
                views = review.views,
                helps = review.helps,
                clickedHelpful = review.clickedHelpful,
                images = review.images,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt,
            )
        }
    }
}

data class ReviewWriterInfoResponse(
    @Schema(description = "작성자 ID", example = "1")
    val id: Long,
    @Schema(description = "작성자 닉네임", example = "말랑")
    val nickname: String,
    @Schema(description = "작성자 프로필 사진 URL", example = "https://이미지.com")
    val profileImageUrl: String?,
) {
    companion object {
        fun from(writer: ReviewWriterInfo): ReviewWriterInfoResponse {
            return ReviewWriterInfoResponse(
                writer.id,
                writer.nickname,
                writer.profileImageUrl,
            )
        }
    }
}
