package com.celuveat.restaurant.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantImageJpaRepository : JpaRepository<RestaurantImageJpaEntity, Long> {
    fun findByRestaurantIn(restaurants: List<RestaurantJpaEntity>): List<RestaurantImageJpaEntity>
}
