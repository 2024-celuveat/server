package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.utils.geometry.SquarePolygon
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomRestaurantRepository {
    fun findAllByFilter(
        filter: RestaurantFilter,
        pageable: Pageable,
    ): Slice<RestaurantJpaEntity>

    fun findAllByFilter(filter: RestaurantFilter): List<RestaurantJpaEntity>

    fun countAllByFilter(filter: RestaurantFilter): Long
}

data class RestaurantFilter(
    val category: String?,
    val region: String?,
    val searchArea: SquarePolygon?,
)
