package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Celebrity

interface ReadCelebritiesPort {
    fun readVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>>

    fun readBestCelebrities(): List<Celebrity>
}
