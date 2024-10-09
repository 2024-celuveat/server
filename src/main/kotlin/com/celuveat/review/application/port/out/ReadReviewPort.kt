package com.celuveat.review.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.domain.Review

interface ReadReviewPort {
    fun readById(reviewId: Long): Review

    fun readAllByRestaurantId(
        restaurantsId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Review>

    fun countByWriterId(memberId: Long): Int

    fun countByRestaurantId(restaurantId: Long): Int

    fun readMyReviews(memberId: Long): List<Review>
}
