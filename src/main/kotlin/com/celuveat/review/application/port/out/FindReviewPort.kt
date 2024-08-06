package com.celuveat.review.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.domain.Review

interface FindReviewPort {

    fun getById(reviewId: Long): Review

    fun findAllByRestaurantId(restaurantsId: Long, page: Int, size: Int): SliceResult<Review>
}
