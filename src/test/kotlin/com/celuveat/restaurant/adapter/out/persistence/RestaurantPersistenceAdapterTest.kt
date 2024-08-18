package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@PersistenceAdapterTest
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
        val visitedRestaurants = restaurantPersistenceAdapter.readVisitedRestaurantByCelebrity(
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
        val mostVisitedRestaurants = restaurantPersistenceAdapter.readCelebrityRecommendRestaurant()

        // then
        mostVisitedRestaurants.size shouldBe 2
        mostVisitedRestaurants.map { it.id } shouldContainInOrder listOf(
            restaurantA.id,
            restaurantB.id,
        )
    }

    test("조건에 따라 음식점을 검색한다.") {
        // given
        restaurantJpaRepository.saveAll(
            sut.giveMeBuilder<RestaurantJpaEntity>()
                .setExp(RestaurantJpaEntity::category, "한식", 2)
                .setExp(RestaurantJpaEntity::roadAddress, "서울", 1)
                .sampleList(5),
        )

        // when
        val restaurants = restaurantPersistenceAdapter.readRestaurantsByCondition(
            category = "한식",
            region = "서울",
            page = 0,
            size = 2,
        )

        // then
        restaurants.size shouldBe 1
        restaurants.hasNext shouldBe false
    }

    test("존재하지 않는 조건은 생략하고 검색한다.") {
        // given
        restaurantJpaRepository.saveAll(
            sut.giveMeBuilder<RestaurantJpaEntity>()
                .setExp(RestaurantJpaEntity::category, "한식", 1)
                .setExp(RestaurantJpaEntity::roadAddress, "서울", 2)
                .sampleList(4),
        )

        // when
        val restaurants = restaurantPersistenceAdapter.readRestaurantsByCondition(
            category = null,
            region = "서울",
            page = 0,
            size = 3,
        )

        // then
        restaurants.size shouldBe 2
        restaurants.contents.map { it.roadAddress } shouldContainInOrder listOf("서울", "서울")
        restaurants.hasNext shouldBe false
    }

    test("최근 업데이트된 음식점을 조회한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::name, "1 음식점")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::name, "2 음식점")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::name, "3 음식점")
                    .sample()
            )
        )
        val savedCelebrity = celebrityJpaRepository.save(sut.giveMeOne())
        celebrityRestaurantJpaRepository.saveAll(
            savedRestaurants.map {
                CelebrityRestaurantJpaEntity(
                    celebrity = savedCelebrity,
                    restaurant = it,
                )
            },
        )
        val baseDate = LocalDate.now()
        val startOfWeek: LocalDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek: LocalDate = baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

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
        val weeklyUpdatedRestaurants = restaurantPersistenceAdapter.readByCreatedDateBetween(
            startOfWeek,
            endOfWeek,
            0,
            3
        )

        // then
        weeklyUpdatedRestaurants.size shouldBe 3
        weeklyUpdatedRestaurants.contents.map { it.name } shouldContainInOrder listOf(
            "3 음식점", "2 음식점", "1 음식점"
        )
    }
})
