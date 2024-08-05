package com.celuveat.review.application.port.`in`.result

import com.celuveat.member.domain.Member
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.Star
import java.time.LocalDateTime

data class ReviewPreviewResult(
    val id: Long,
    val restaurantId: Long,
    val writer: ReviewWriterInfo,
    var content: String,
    var star: Star,
    var views: Long,
    var helps: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = createdAt
) {

    companion object {
        fun from(review: Review): ReviewPreviewResult {
            return ReviewPreviewResult(
                id = review.id,
                restaurantId = review.restaurant.id,
                writer = ReviewWriterInfo.from(review.writer),
                content = review.content,
                star = review.star,
                views = review.views,
                helps = review.helps,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }
}

data class SingleReviewResult(
    val id: Long,
    val restaurantId: Long,
    val writer: ReviewWriterInfo,
    var content: String,
    var star: Star,
    var views: Long,
    var helps: Long,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = createdAt
) {
    companion object {
        fun from(review: Review): SingleReviewResult {
            return SingleReviewResult(
                id = review.id,
                restaurantId = review.restaurant.id,
                writer = ReviewWriterInfo.from(review.writer),
                content = review.content,
                star = review.star,
                views = review.views,
                helps = review.helps,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }
}

data class ReviewWriterInfo(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String?,
) {
    companion object {
        fun from(writer: Member): ReviewWriterInfo {
            return ReviewWriterInfo(
                writer.id,
                writer.nickname,
                writer.profileImageUrl
            )
        }
    }
}
