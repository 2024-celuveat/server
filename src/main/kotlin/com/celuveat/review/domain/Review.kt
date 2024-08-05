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
    var star: Star,  // 별점
    var views: Long = 0,  // 조회수
    var helps: Long = 0,  // '도움돼요' 수.
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = createdAt
) {
    fun validateWriter(member: Member) {
        if (writer != member) {
            throw NoAuthorityReviewException
        }
    }

    fun update(content: String, star: Star) {
        this.content = content;
        this.star = star;
        this.updatedAt = LocalDateTime.now()
    }
}
