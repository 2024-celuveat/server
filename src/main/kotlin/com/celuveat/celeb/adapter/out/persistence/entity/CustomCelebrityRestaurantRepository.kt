package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.restaurant.adapter.`in`.rest.request.ReadCelebrityVisitedRestaurantSortCondition
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomCelebrityRestaurantRepository {
    fun findRestaurantsByCelebrityId(
        celebrityId: Long,
        pageable: Pageable,
        sort: ReadCelebrityVisitedRestaurantSortCondition,
    ): Slice<RestaurantJpaEntity>
}
