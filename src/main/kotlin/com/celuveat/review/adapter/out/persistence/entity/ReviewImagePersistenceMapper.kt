package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.review.domain.ReviewImage

@Mapper
class ReviewImagePersistenceMapper(
) {
    fun toEntity(review: ReviewJpaEntity, reviewImage: ReviewImage): ReviewImageJpaEntity {
        return ReviewImageJpaEntity(
            review = review,
            imageUrl = reviewImage.imageUrl
        )
    }
}
