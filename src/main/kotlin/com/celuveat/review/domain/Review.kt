package com.celuveat.review.domain

import com.celuveat.member.domain.Member
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.review.exception.NoAuthorityReviewException
import java.time.LocalDateTime

class Review(
    val id: Long = 0,
    val restaurant: Restaurant,
    val writer: Member,
    var content: String,
    // 조회수
    var star: Star,
    // 조회수
    var views: Long = 0,
    // '도움돼요' 수.
    var helps: Long = 0,
    var images: List<ReviewImage>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = createdAt,
) {
    fun validateWriter(member: Member) {
        if (writer != member) {
            throw NoAuthorityReviewException
        }
    }

    fun update(
        content: String,
        star: Star,
        images: List<ReviewImage>,
    ) {
        this.content = content
        this.star = star
        this.updatedAt = LocalDateTime.now()
        this.images = images
    }

    fun increaseView() {
        views += 1
    }

    fun clickHelpful(member: Member): HelpfulReview {
        helps += 1
        return HelpfulReview(review = this, member = member)
    }

    fun unClickHelpful() {
        helps -= 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
