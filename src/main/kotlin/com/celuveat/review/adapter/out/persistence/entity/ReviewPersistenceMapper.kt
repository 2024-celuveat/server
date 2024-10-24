package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.ReviewImage
import com.celuveat.review.domain.Star

@Mapper
class ReviewPersistenceMapper(
    private val memberPersistenceMapper: MemberPersistenceMapper,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
) {
    fun toDomain(
        review: ReviewJpaEntity,
        images: List<ReviewImageJpaEntity>,
    ): Review {
        return Review(
            id = review.id,
            restaurant = restaurantPersistenceMapper.toDomainWithoutImage(review.restaurant),
            writer = memberPersistenceMapper.toDomain(review.writer),
            content = review.content,
            star = Star.from(review.star),
            views = review.views,
            helps = review.helps,
            createdAt = review.createdAt,
            updatedAt = review.updatedAt,
            images = images.map { ReviewImage(it.imageUrl) },
        )
    }

    fun toEntity(review: Review): ReviewJpaEntity {
        return ReviewJpaEntity(
            id = review.id,
            restaurant = restaurantPersistenceMapper.toEntity(review.restaurant),
            writer = memberPersistenceMapper.toEntity(review.writer),
            content = review.content,
            star = review.star.score,
            views = review.views,
            helps = review.helps,
            hasPhoto = review.images.isNotEmpty(),
            createdAt = review.createdAt,
            updatedAt = review.updatedAt,
        )
    }
}
