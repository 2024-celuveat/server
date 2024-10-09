package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.review.exception.NotFoundReviewException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long> {
    override fun getById(id: Long): ReviewJpaEntity {
        return findByIdOrThrow(id) { NotFoundReviewException }
    }

    fun findAllByRestaurantId(
        restaurantsId: Long,
        page: Pageable,
    ): Slice<ReviewJpaEntity>

    fun countByWriterId(writerId: Long): Long

    fun countByRestaurantId(restaurantId: Long): Long

    fun findAllByWriterId(writerId: Long): List<ReviewJpaEntity>
}
