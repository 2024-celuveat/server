package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomReviewRepositoryImpl(
    private val executor: KotlinJdslJpqlExecutor,
) : CustomReviewRepository {

    override fun findAllByRestaurantId(
        restaurantsId: Long,
        onlyPhotoReview: Boolean,
        page: Pageable
    ): Slice<ReviewJpaEntity> {
        val findSlice = executor.findSlice(page) {
            select(
                entity(ReviewJpaEntity::class),
            ).from(
                entity(ReviewJpaEntity::class),
            ).whereAnd(
                path(ReviewJpaEntity::restaurant)
                    .path(RestaurantJpaEntity::id)
                    .eq(restaurantsId),
                if (onlyPhotoReview) {
                    path(ReviewJpaEntity::hasPhoto).eq(true)
                } else {
                    null
                }
            ).orderBy(
                path(CelebrityRestaurantJpaEntity::id).desc()
            )
        }
        val restaurants = findSlice.content.filterNotNull()
        return SliceImpl(
            restaurants,
            findSlice.pageable,
            findSlice.hasNext(),
        )

    }
}
