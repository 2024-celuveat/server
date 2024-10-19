package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.review.exception.NotFoundReviewException
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long>, CustomReviewRepository {
    override fun getById(id: Long): ReviewJpaEntity {
        return findByIdOrThrow(id) { NotFoundReviewException }
    }

    fun countByWriterId(writerId: Long): Long

    fun countByRestaurantId(restaurantId: Long): Long

    fun findAllByWriterId(writerId: Long): List<ReviewJpaEntity>
}
