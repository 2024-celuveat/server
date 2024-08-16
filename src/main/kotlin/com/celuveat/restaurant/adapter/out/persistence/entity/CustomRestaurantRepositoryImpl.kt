package com.celuveat.restaurant.adapter.out.persistence.entity

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomRestaurantRepositoryImpl(
    private val executor: KotlinJdslJpqlExecutor,
) : CustomRestaurantRepository {
    override fun findAllByFilter(
        filter: RestaurantFilter,
        pageable: Pageable
    ): Slice<RestaurantJpaEntity> {
        val findSlice = executor.findSlice(pageable) {
            select(
                entity(RestaurantJpaEntity::class),
            ).from(
                entity(RestaurantJpaEntity::class),
            ).whereAnd(
                filter.category?.let { path(RestaurantJpaEntity::category).eq(it) },
                filter.region?.let { path(RestaurantJpaEntity::roadAddress).like("%$it%") },
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
