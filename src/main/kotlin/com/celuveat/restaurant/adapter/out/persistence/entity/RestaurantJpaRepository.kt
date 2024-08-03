package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.restaurant.exception.NotFoundRestaurantException
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantJpaRepository : JpaRepository<RestaurantJpaEntity, Long> {
    override fun getById(id: Long): RestaurantJpaEntity {
        return findByIdOrThrow(id) { NotFoundRestaurantException }
    }
}
