package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.review.adapter.`in`.rest.request.ReadReviewSortCondition
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CustomReviewRepository {
    fun findAllByRestaurantId(
        restaurantsId: Long,
        onlyPhotoReview: Boolean,
        sort: ReadReviewSortCondition,
        page: Pageable,
    ): Slice<ReviewJpaEntity>
}
