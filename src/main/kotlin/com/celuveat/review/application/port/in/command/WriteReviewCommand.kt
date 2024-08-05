package com.celuveat.review.application.port.`in`.command

import com.celuveat.member.domain.Member
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.Star

data class WriteReviewCommand(
    val memberId: Long,
    val restaurantId: Long,
    var content: String,
    var star: Star,
) {
    fun toReview(member: Member, restaurant: Restaurant): Review {
        return Review(
            restaurant = restaurant,
            writer = member,
            content = content,
            star = star,
        )
    }
}
