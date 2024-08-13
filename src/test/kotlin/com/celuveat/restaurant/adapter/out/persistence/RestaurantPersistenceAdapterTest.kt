package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(RestaurantPersistenceAdapter::class, RestaurantPersistenceMapper::class, JpaConfig::class)
@DataJpaTest
class RestaurantPersistenceAdapterTest(
    private val restaurantPersistenceAdapter: RestaurantPersistenceAdapter,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityRestaurantJpaRepository: CelebrityRestaurantJpaRepository,
) : FunSpec({
    test("셀럽이 방문한 음식점을 조회한다.") {
        // given
        val savedRestaurants =
            restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(3))
        val savedCelebrity = celebrityJpaRepository.save(sut.giveMeOne())
        celebrityRestaurantJpaRepository.saveAll(
            savedRestaurants.map {
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrity,
                    restaurant = it,
                )
            },
        )

        restaurantImageJpaRepository.saveAll(
            savedRestaurants.map {
                sut.giveMeBuilder<RestaurantImageJpaEntity>()
                    .set(RestaurantImageJpaEntity::id, 0)
                    .set(RestaurantImageJpaEntity::restaurant, it)
                    .set(RestaurantImageJpaEntity::isThumbnail, true, 1)
                    .sampleList(3)
            }.flatten(),
        )

        // when
        val visitedRestaurants = restaurantPersistenceAdapter.findVisitedRestaurantByCelebrity(
            celebrityId = savedCelebrity.id,
            page = 0,
            size = 2,
        )

        // then
        visitedRestaurants.size shouldBe 2
        visitedRestaurants.contents.map { it.id } shouldContainInOrder listOf(
            savedRestaurants[2].id,
            savedRestaurants[1].id,
        )
        visitedRestaurants.hasNext shouldBe true
    }

    test("셀럽이 많이 다녀간 순서로 음식점을 조회한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(3))
        val restaurantA = savedRestaurants[0]
        val restaurantB = savedRestaurants[1]
        celebrityRestaurantJpaRepository.saveAll(
            listOf(
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrities[0],
                    restaurant = restaurantA,
                ),
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrities[1],
                    restaurant = restaurantA,
                ),
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrities[2],
                    restaurant = restaurantA,
                ),
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrities[0],
                    restaurant = restaurantB,
                ),
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrities[1],
                    restaurant = restaurantB,
                ),
            ),
        ) // 음식점 A는 3명, 음식점 B는 2명의 셀럽이 방문

        restaurantImageJpaRepository.saveAll(
            savedRestaurants.map {
                sut.giveMeBuilder<RestaurantImageJpaEntity>()
                    .set(RestaurantImageJpaEntity::id, 0)
                    .set(RestaurantImageJpaEntity::restaurant, it)
                    .set(RestaurantImageJpaEntity::isThumbnail, true, 1)
                    .sampleList(3)
            }.flatten(),
        )

        // when
        val mostVisitedRestaurants = restaurantPersistenceAdapter.findCelebrityRecommendRestaurant()

        // then
        mostVisitedRestaurants.size shouldBe 2
        mostVisitedRestaurants.map { it.id } shouldContainInOrder listOf(
            restaurantA.id,
            restaurantB.id,
        )
    }
})
