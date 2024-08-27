package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Celebrity

interface ReadCelebritiesPort {
    fun readVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>>

    fun readBestCelebrities(): List<Celebrity>

    fun readById(celebrityId: Long): Celebrity

    fun readByYoutubeContentIds(youtubeContentIds: List<Long>): List<Celebrity>
}
