package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.restaurant.adapter.`in`.rest.request.ReadCelebrityVisitedRestaurantSortCondition
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomCelebrityRestaurantRepositoryImpl(
    private val executor: KotlinJdslJpqlExecutor,
) : CustomCelebrityRestaurantRepository {

    override fun findRestaurantsByCelebrityId(
        celebrityId: Long,
        pageable: Pageable,
        sort: ReadCelebrityVisitedRestaurantSortCondition
    ): Slice<RestaurantJpaEntity> {
        val findSlice = executor.findSlice(pageable) {
            select(
                entity(RestaurantJpaEntity::class),
            ).from(
                entity(CelebrityRestaurantJpaEntity::class),
                fetchJoin(CelebrityRestaurantJpaEntity::restaurant),
            ).whereAnd(
                path(CelebrityRestaurantJpaEntity::celebrity)
                    .path(CelebrityJpaEntity::id)
                    .eq(celebrityId),
            ).orderBy(
                when (sort) {
                    ReadCelebrityVisitedRestaurantSortCondition.CREATED_AT -> path(RestaurantJpaEntity::createdAt).desc()
                    ReadCelebrityVisitedRestaurantSortCondition.REVIEW -> path(RestaurantJpaEntity::reviewCount).desc()
                    ReadCelebrityVisitedRestaurantSortCondition.LIKE -> path(RestaurantJpaEntity::likeCount).desc()
                }
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
