package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class CustomRestaurantRepositoryImpl(
    private val executor: KotlinJdslJpqlExecutor,
    private val entityManager: EntityManager,
    private val context: JpqlRenderContext,
    private val renderer: JpqlRenderer,
) : CustomRestaurantRepository {
    override fun findAllByFilter(
        filter: RestaurantFilter,
        pageable: Pageable,
    ): Slice<RestaurantJpaEntity> {
        val findSlice = executor.findSlice(pageable) {
            select(
                entity(RestaurantJpaEntity::class),
            ).from(
                entity(RestaurantJpaEntity::class),
                leftJoin(CelebrityRestaurantJpaEntity::class).on(
                    path(CelebrityRestaurantJpaEntity::restaurant)(RestaurantJpaEntity::id)
                        .eq(path(RestaurantJpaEntity::id)),
                ),
            ).whereAnd(
                filter.category?.let { path(RestaurantJpaEntity::category).eq(it) },
                filter.region?.let { path(RestaurantJpaEntity::roadAddress).like("%$it%") },
                filter.searchArea?.let {
                    path(RestaurantJpaEntity::longitude).between(
                        it.lowLongitude,
                        it.highLongitude,
                    )
                },
                filter.searchArea?.let { path(RestaurantJpaEntity::latitude).between(it.lowLatitude, it.highLatitude) },
                filter.celebrityId?.let { path(CelebrityRestaurantJpaEntity::celebrity)(CelebrityJpaEntity::id).eq(it) },
            )
        }
        val restaurants = findSlice.content.filterNotNull()
        return SliceImpl(
            restaurants,
            findSlice.pageable,
            findSlice.hasNext(),
        )
    }

    override fun findAllByFilter(filter: RestaurantFilter): List<RestaurantJpaEntity> {
        val query = jpql {
            select(
                entity(RestaurantJpaEntity::class),
            ).from(
                entity(RestaurantJpaEntity::class),
            ).whereAnd(
                filter.category?.let { path(RestaurantJpaEntity::category).eq(it) },
                filter.region?.let { path(RestaurantJpaEntity::roadAddress).like("%$it%") },
                filter.searchArea?.let {
                    path(RestaurantJpaEntity::longitude).between(
                        it.lowLongitude,
                        it.highLongitude,
                    )
                },
                filter.searchArea?.let { path(RestaurantJpaEntity::latitude).between(it.lowLatitude, it.highLatitude) },
            )
        }
        val rendered = renderer.render(query, context)
        return entityManager.createQuery(rendered.query, RestaurantJpaEntity::class.java)
            .apply { rendered.params.forEach { (name, value) -> setParameter(name, value) } }
            .resultList
    }

    override fun countAllByFilter(filter: RestaurantFilter): Long {
        val query = jpql {
            select(
                count(entity(RestaurantJpaEntity::class)),
            ).from(
                entity(RestaurantJpaEntity::class),
            ).whereAnd(
                filter.category?.let { path(RestaurantJpaEntity::category).eq(it) },
                filter.region?.let { path(RestaurantJpaEntity::roadAddress).like("%$it%") },
                filter.searchArea?.let {
                    path(RestaurantJpaEntity::longitude).between(
                        it.lowLongitude,
                        it.highLongitude,
                    )
                },
                filter.searchArea?.let { path(RestaurantJpaEntity::latitude).between(it.lowLatitude, it.highLatitude) },
            )
        }

        val rendered = renderer.render(query, context)
        return entityManager.createQuery(rendered.query, Long::class.java)
            .apply { rendered.params.forEach { (name, value) -> setParameter(name, value) } }
            .singleResult
    }
}
