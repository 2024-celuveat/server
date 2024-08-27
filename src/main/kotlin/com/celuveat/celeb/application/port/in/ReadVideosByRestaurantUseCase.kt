package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.result.VideoWithCelebrityResult

interface ReadVideosByRestaurantUseCase {
    fun readVideosByRestaurant(restaurantId: Long): List<VideoWithCelebrityResult>
}
