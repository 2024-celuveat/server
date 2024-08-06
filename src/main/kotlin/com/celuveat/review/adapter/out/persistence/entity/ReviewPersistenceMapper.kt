package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.Star

@Mapper
class ReviewPersistenceMapper(
    private val memberPersistenceMapper: MemberPersistenceMapper,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
) {
    fun toDomain(
        entity: ReviewJpaEntity,
    ): Review {
        return Review(
            id = entity.id,
            restaurant = restaurantPersistenceMapper.toDomainWithoutImage(entity.restaurant),
            writer = memberPersistenceMapper.toDomain(entity.writer),
            content = entity.content,
            star = Star.from(entity.star),
            views = entity.views,
            helps = entity.helps,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
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
            createdAt = review.createdAt,
            updatedAt = review.updatedAt
        )
    }
}
