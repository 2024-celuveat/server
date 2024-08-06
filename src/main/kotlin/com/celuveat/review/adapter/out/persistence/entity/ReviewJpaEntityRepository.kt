package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.review.exception.NotFoundReviewException
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaEntityRepository : JpaRepository<ReviewJpaEntity, Long> {

    override fun getById(id: Long): ReviewJpaEntity {
        return findByIdOrThrow(id) { NotFoundReviewException }
    }
}
