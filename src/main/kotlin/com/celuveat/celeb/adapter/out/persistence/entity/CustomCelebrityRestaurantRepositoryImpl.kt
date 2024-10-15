package com.celuveat.celeb.adapter.out.persistence.entity

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
    ): Slice<RestaurantJpaEntity> {
        val findSlice = executor.findSlice(pageable) {
            select(
                entity(RestaurantJpaEntity::class),
            ).from(
                entity(CelebrityRestaurantJpaEntity::class),
                fetchJoin(RestaurantJpaEntity::class).on(
                    path(CelebrityRestaurantJpaEntity::restaurant)(RestaurantJpaEntity::id).eq(
                        path(RestaurantJpaEntity::id)
                    ),
                ),
            ).whereAnd(
                path(CelebrityRestaurantJpaEntity::celebrity)
                    .path(CelebrityJpaEntity::id)
                    .eq(celebrityId),
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
