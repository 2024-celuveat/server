package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Video

interface ReadVideoPort {
    fun readVideosInRestaurant(restaurantId: Long): List<Video>
}
