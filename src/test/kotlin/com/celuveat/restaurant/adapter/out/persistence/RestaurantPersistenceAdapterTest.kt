package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadCelebrityVisitedRestaurantSortCondition.CREATED_AT
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
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
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@PersistenceAdapterTest
class RestaurantPersistenceAdapterTest(
    private val restaurantPersistenceAdapter: RestaurantPersistenceAdapter,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val interestedRestaurantJpaRepository: InterestedRestaurantJpaRepository,
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
            sort = CREATED_AT,
        )

        // then
        visitedRestaurants.size shouldBe 2
        visitedRestaurants.contents.map { it.id } shouldContainInOrder listOf(
            savedRestaurants[2].id,
            savedRestaurants[1].id,
        )
        visitedRestaurants.hasNext shouldBe true
    }

    test("셀럽이 방문한 음식점 조회 시 페이지에 따른 중복 데이터가 존재하지 않는다.") {
        // given
        val savedRestaurants =
            restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(20))
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
        listOf(
            restaurantPersistenceAdapter.readVisitedRestaurantByCelebrity(
                celebrityId = savedCelebrity.id,
                page = 0,
                size = 2,
                sort = CREATED_AT,
            ).contents + restaurantPersistenceAdapter.readVisitedRestaurantByCelebrity(
                celebrityId = savedCelebrity.id,
                page = 1,
                size = 2,
                sort = CREATED_AT,
            ).contents + restaurantPersistenceAdapter.readVisitedRestaurantByCelebrity(
                celebrityId = savedCelebrity.id,
                page = 2,
                size = 2,
                sort = CREATED_AT,
            ).contents + restaurantPersistenceAdapter.readVisitedRestaurantByCelebrity(
                celebrityId = savedCelebrity.id,
                page = 3,
                size = 2,
                sort = CREATED_AT,
            ).contents,
        ).flatten().distinctBy { it.id }.size shouldBe 8
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

    test("조건에 따라 음식점을 페이징 검색한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(
            sut.giveMeBuilder<RestaurantJpaEntity>()
                .setExp(RestaurantJpaEntity::category, "한식", 2)
                .setExp(RestaurantJpaEntity::roadAddress, "서울", 1)
                .sampleList(5),
        )
        savedRestaurants.map {
            celebrityRestaurantJpaRepository.save(
                sut.giveMeBuilder<CelebrityRestaurantJpaEntity>()
                    .set(CelebrityRestaurantJpaEntity::restaurant, it)
                    .set(
                        CelebrityRestaurantJpaEntity::celebrity,
                        celebrityJpaRepository.save(sut.giveMeOne<CelebrityJpaEntity>()),
                    )
                    .sample(),
            )
        }

        // when
        val restaurants = restaurantPersistenceAdapter.readRestaurantsByCondition(
            category = "한식",
            region = "서울",
            searchArea = null,
            celebrityId = null,
            page = 0,
            size = 2,
        )

        // then
        restaurants.size shouldBe 1
        restaurants.hasNext shouldBe false
    }

    test("조건에 따른 음식점을 전체 검색한다.") {
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
            searchArea = null,
            celebrityId = null,
        )

        // then
        restaurants.size shouldBe 1
    }

    test("존재하지 않는 조건은 생략하고 검색한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(
            sut.giveMeBuilder<RestaurantJpaEntity>()
                .setExp(RestaurantJpaEntity::category, "한식", 1)
                .setExp(RestaurantJpaEntity::roadAddress, "서울", 2)
                .sampleList(5),
        )

        savedRestaurants.map {
            celebrityRestaurantJpaRepository.save(
                sut.giveMeBuilder<CelebrityRestaurantJpaEntity>()
                    .set(CelebrityRestaurantJpaEntity::restaurant, it)
                    .set(
                        CelebrityRestaurantJpaEntity::celebrity,
                        celebrityJpaRepository.save(sut.giveMeOne<CelebrityJpaEntity>()),
                    )
                    .sample(),
            )
        }

        // when
        val restaurants = restaurantPersistenceAdapter.readRestaurantsByCondition(
            category = null,
            region = "서울",
            searchArea = null,
            celebrityId = null,
            page = 0,
            size = 3,
        )

        // then
        restaurants.size shouldBe 2
        restaurants.contents.map { it.roadAddress } shouldContainInOrder listOf("서울", "서울")
        restaurants.hasNext shouldBe false
    }

    test("조건에 맞는 음식점의 개수를 조회 한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(
            sut.giveMeBuilder<RestaurantJpaEntity>()
                .setExp(RestaurantJpaEntity::category, "한식", 2)
                .setExp(RestaurantJpaEntity::roadAddress, "서울", 1)
                .sampleList(5),
        )
        savedRestaurants.map {
            celebrityRestaurantJpaRepository.save(
                sut.giveMeBuilder<CelebrityRestaurantJpaEntity>()
                    .set(CelebrityRestaurantJpaEntity::restaurant, it)
                    .set(
                        CelebrityRestaurantJpaEntity::celebrity,
                        celebrityJpaRepository.save(sut.giveMeOne<CelebrityJpaEntity>()),
                    )
                    .sample(),
            )
        }

        // when
        val count = restaurantPersistenceAdapter.countRestaurantsByCondition(
            category = "한식",
            region = "서울",
            searchArea = null,
            celebrityId = null,
        )

        // then
        count shouldBe 1
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
                    .sample(),
            ),
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
        val weeklyUpdatedRestaurants = restaurantPersistenceAdapter.readByCreatedAtBetween(
            startOfWeek,
            endOfWeek,
            0,
            3,
        )

        // then
        weeklyUpdatedRestaurants.size shouldBe 3
        weeklyUpdatedRestaurants.contents.map { it.name } shouldContainInOrder listOf(
            "3 음식점", "2 음식점", "1 음식점",
        )
    }

    test("음식점을 기준으로 주변 음식점을 조회한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.0)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.001)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.002)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.003)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.004)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.005)
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .set(RestaurantJpaEntity::latitude, 37.5)
                    .set(RestaurantJpaEntity::longitude, 127.006)
                    .sample(),
            ),
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
        val restaurants = restaurantPersistenceAdapter.readNearby(savedRestaurants[0].id)

        // then
        restaurants.size shouldBe 5
        restaurants.map { it.id } shouldNotContain savedRestaurants[0].id
    }

    test("기간내 관심 등록이 많은 음식점을 조회한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        val restaurantA = savedRestaurants[0]
        val restaurantB = savedRestaurants[1]

        val savedMember = memberJpaRepository.saveAll(sut.giveMeBuilder<MemberJpaEntity>().sampleList(3))
        val memberA = savedMember[0]
        val memberB = savedMember[1]
        val memberC = savedMember[2]
        interestedRestaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, memberA)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[1])
                    .sample(),
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, memberB)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[1])
                    .sample(),
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, memberC)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[1])
                    .sample(),
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, memberA)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[0])
                    .sample(),
            ),
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
        val interestedRestaurants = restaurantPersistenceAdapter.readTop10InterestedRestaurantsInDate(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1),
        )

        // then
        interestedRestaurants.size shouldBe 2
        interestedRestaurants.map { it.id } shouldContainAll listOf(restaurantB.id, restaurantA.id)
    }

    test("이름으로 음식점을 조회한다.") {
        // given
        restaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "말랑이의 감자탕")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "말랑이의감자탕")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "말랑이의 삼계탕")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "로이스의 감자탕")
                    .sample(),
            ),
        )

        // when
        val result1 = restaurantPersistenceAdapter.readByName("감자")
        val result2 = restaurantPersistenceAdapter.readByName("말랑")
        val result3 = restaurantPersistenceAdapter.readByName("의 감")

        // then
        result1.size shouldBe 3
        result2.size shouldBe 3
        result3.size shouldBe 2
    }

    test("카테고리를 검색 한다.") {
        // given
        restaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "말랑이의 감자탕")
                    .setExp(RestaurantJpaEntity::category, "찌개, 국, 탕")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "말랑이의국밥")
                    .setExp(RestaurantJpaEntity::category, "국밥")
                    .sample(),
                sut.giveMeBuilder<RestaurantJpaEntity>()
                    .setExp(RestaurantJpaEntity::name, "로이스의 닭한마리")
                    .setExp(RestaurantJpaEntity::category, "국물요리")
                    .sample(),
            ),
        )

        // when
        val categories = restaurantPersistenceAdapter.readCategoriesByKeyword("국")

        // then
        categories.size shouldBe 3
    }
})
