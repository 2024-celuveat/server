package com.celuveat.restaurant.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantJpaRepository : JpaRepository<RestaurantJpaEntity, Long> {
    fun findAllByIdIn(id: List<Long>): List<RestaurantJpaEntity>
}
