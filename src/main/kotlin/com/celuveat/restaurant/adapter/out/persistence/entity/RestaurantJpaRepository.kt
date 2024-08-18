package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.restaurant.exception.NotFoundRestaurantException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RestaurantJpaRepository : JpaRepository<RestaurantJpaEntity, Long>, CustomRestaurantRepository {
    override fun getById(id: Long): RestaurantJpaEntity {
        return findByIdOrThrow(id) { NotFoundRestaurantException }
    }

    @Query("SELECT r FROM RestaurantJpaEntity r ORDER BY r.createdAt DESC")
    fun findLatest(pageable: Pageable): Slice<RestaurantJpaEntity>
}
