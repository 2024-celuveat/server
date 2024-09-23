package com.celuveat.review.application.port.`in`

interface ReadAmountOfRestaurantReviewsUseCase {
    fun readAmountOfRestaurantReviews(restaurantId: Long): Int
}
