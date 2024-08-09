package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Celebrity

interface FindCelebritiesPort {
    fun findVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>>

    fun findBestCelebrities(): List<Celebrity>
}
