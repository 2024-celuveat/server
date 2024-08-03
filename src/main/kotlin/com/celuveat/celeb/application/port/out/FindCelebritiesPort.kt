package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Celebrity

interface FindCelebritiesPort {
    fun findInterestedCelebrities(memberId: Long): List<Celebrity>

    fun findVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>>
}
