package com.celuveat.review.adapter.out.persistence.entity

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomReviewRepository {

    fun findAllByRestaurantId(
        restaurantsId: Long,
        onlyPhotoReview: Boolean,
        page: Pageable,
    ): Slice<ReviewJpaEntity>
}
