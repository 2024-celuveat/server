package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadPopularRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.InterestedRestaurant
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify

class RestaurantQueryServiceTest : BehaviorSpec({
    val readRestaurantPort: ReadRestaurantPort = mockk()
    val readCelebritiesPort: ReadCelebritiesPort = mockk()
    val readInterestedRestaurantPort: ReadInterestedRestaurantPort = mockk()

    val restaurantQueryService = RestaurantQueryService(
        readRestaurantPort,
        readCelebritiesPort,
        readInterestedRestaurantPort,
    )

    Given("관심 음식점을 조회할 때") {
        val memberId = 1L
        val page = 0
        val size = 2
        val interestedRestaurantResult = SliceResult.of(
            contents = sut.giveMeBuilder<InterestedRestaurant>().sampleList(3),
            currentPage = 0,
            hasNext = false,
        )
        val interestedRestaurantResultIds = interestedRestaurantResult.contents.map { it.restaurant.id }
        every {
            readInterestedRestaurantPort.readInterestedRestaurants(
                memberId,
                page,
                size,
            )
        } returns interestedRestaurantResult
        every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(interestedRestaurantResultIds) } returns mapOf(
            interestedRestaurantResultIds[0] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
                .sampleList(2),
            interestedRestaurantResultIds[1] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                .sampleList(2),
            interestedRestaurantResultIds[2] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                .sampleList(1),
        )
        When("회원이 관심 식당을 조회하면") {
            val readInterestedRestaurantsQuery = ReadInterestedRestaurantsQuery(
                memberId = memberId,
                page = page,
                size = size,
            )
            val interestedRestaurants = restaurantQueryService.readInterestedRestaurant(readInterestedRestaurantsQuery)

            Then("관심 식당 목록을 반환한다") {
                interestedRestaurants.contents.size shouldBe 3
                interestedRestaurants.contents[0].visitedCelebrities.size shouldBe 2
                interestedRestaurants.hasNext shouldBe false
            }
        }
    }

    Given("셀럽이 방문한 음식점을 조회할 때") {
        val celebrityId = 1L
        val page = 0
        val size = 2
        val visitedRestaurantResult = SliceResult.of(
            contents = sut.giveMeBuilder<Restaurant>().sampleList(2),
            currentPage = 0,
            hasNext = false,
        )
        val restaurantIds = visitedRestaurantResult.contents.map { it.id }
        When("회원이 음식점을 조회하면") {
            every {
                readRestaurantPort.readVisitedRestaurantByCelebrity(
                    celebrityId,
                    page,
                    size,
                )
            } returns visitedRestaurantResult
            val memberId = 1L
            val interestedRestaurants = listOf(
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, visitedRestaurantResult.contents[0])
                    .sample(),
            )
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns interestedRestaurants
            val readCelebrityVisitedRestaurantQuery = ReadCelebrityVisitedRestaurantQuery(
                memberId = memberId,
                celebrityId = celebrityId,
                page = page,
                size = size,
            )
            val visitedRestaurants =
                restaurantQueryService.readCelebrityVisitedRestaurant(readCelebrityVisitedRestaurantQuery)

            Then("관심 등록 여부가 포함되어 응답한다") {
                visitedRestaurants.contents.size shouldBe 2
                visitedRestaurants.contents[0].liked shouldBe true
            }
        }

        When("비회원이 음식점을 조회하면") {
            every {
                readRestaurantPort.readVisitedRestaurantByCelebrity(
                    celebrityId,
                    page,
                    size,
                )
            } returns visitedRestaurantResult
            val readCelebrityVisitedRestaurantQuery = ReadCelebrityVisitedRestaurantQuery(
                memberId = null,
                celebrityId = celebrityId,
                page = page,
                size = size,
            )
            val visitedRestaurants =
                restaurantQueryService.readCelebrityVisitedRestaurant(readCelebrityVisitedRestaurantQuery)

            Then("관심 등록 여부는 false로 응답한다") {
                visitedRestaurants.contents.size shouldBe 2
                visitedRestaurants.contents.map { it.liked } shouldBe listOf(false, false)
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("셀럽 추천 음식점 조회 시") {
        val restaurants = sut.giveMeBuilder<Restaurant>().sampleList(2)
        val restaurantIds = restaurants.map { it.id }
        val celebritiesByRestaurants = mapOf(
            restaurantIds[0] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
                .sampleList(2),
            restaurantIds[1] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                .sampleList(1),
        )
        When("회원이 추천 음식점을 조회하면") {
            val memberId = 1L
            every { readRestaurantPort.readCelebrityRecommendRestaurant() } returns restaurants
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurants
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns listOf(
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, restaurants[0])
                    .sample(),
            ) // 첫 번째 음식점만 관심 등록

            val readCelebrityRecommendRestaurantsQuery = ReadCelebrityRecommendRestaurantsQuery(memberId = memberId)
            val recommendRestaurants = restaurantQueryService.readCelebrityRecommendRestaurants(
                readCelebrityRecommendRestaurantsQuery,
            )

            Then("관심 등록 여부가 포함되어 응답한다") {
                recommendRestaurants.size shouldBe 2
                recommendRestaurants[0].liked shouldBe true
                recommendRestaurants[0].visitedCelebrities.size shouldBe 2
                recommendRestaurants[1].liked shouldBe false
                recommendRestaurants[1].visitedCelebrities.size shouldBe 1
            }
        }

        When("비회원이 추천 음식점을 조회하면") {
            every { readRestaurantPort.readCelebrityRecommendRestaurant() } returns restaurants
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurants
            val readCelebrityRecommendRestaurantsQuery = ReadCelebrityRecommendRestaurantsQuery(memberId = null)
            val recommendRestaurants = restaurantQueryService.readCelebrityRecommendRestaurants(
                readCelebrityRecommendRestaurantsQuery,
            )

            Then("관심 등록 여부는 false로 응답한다") {
                recommendRestaurants.size shouldBe 2
                recommendRestaurants.map { it.liked } shouldBe listOf(false, false)
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("조건에 따라 음식점 조회 시") {
        val restaurants = sut.giveMeBuilder<Restaurant>()
            .sampleList(2)
        val restaurantIds = restaurants.map { it.id }
        val celebritiesByRestaurant = restaurants.associate {
            it.id to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
                .sampleList(1)
        }

        When("회원이 음식점을 조회하면") {
            val memberId = 1L
            val interestedRestaurants = restaurants.map {
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, it)
                    .sample()
            }
            val sliceResult = SliceResult.of(
                contents = restaurants,
                currentPage = 0,
                hasNext = false,
            )

            every {
                readRestaurantPort.readRestaurantsByCondition(
                    category = "한식",
                    region = "서울",
                    searchArea = null,
                    page = 0,
                    size = 5,
                )
            } returns sliceResult
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurant
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId = memberId,
                    restaurantIds = restaurantIds,
                )
            } returns interestedRestaurants

            val readRestaurantsQuery = ReadRestaurantsQuery(
                memberId = memberId,
                category = "한식",
                region = "서울",
                searchArea = null,
                page = 0,
                size = 5,
            )
            val results = restaurantQueryService.readRestaurants(readRestaurantsQuery)

            Then("관심 등록 여부가 포함되어 응답한다") {
                results.contents.size shouldBe 2
                results.contents.forAll { it.liked shouldBe true }
                results.hasNext shouldBe false
            }
        }

        When("비회원이 음식점을 조회하면") {
            val sliceResult = SliceResult.of(
                contents = restaurants,
                currentPage = 0,
                hasNext = false,
            )
            every {
                readRestaurantPort.readRestaurantsByCondition(
                    category = "한식",
                    region = "서울",
                    searchArea = null,
                    page = 0,
                    size = 5,
                )
            } returns sliceResult
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurant

            val readRestaurantsQuery = ReadRestaurantsQuery(
                memberId = null,
                category = "한식",
                region = "서울",
                searchArea = null,
                page = 0,
                size = 5,
            )
            val results = restaurantQueryService.readRestaurants(readRestaurantsQuery)
            Then("관심 등록 여부는 false로 응답한다") {
                results.contents.size shouldBe 2
                results.contents.forAll { it.liked shouldBe false }
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("최근 업데이트된 음식점 조회 시") {
        val restaurants = SliceResult.of(
            contents = sut.giveMeBuilder<Restaurant>().sampleList(2),
            currentPage = 0,
            hasNext = false,
        )
        val restaurantIds = restaurants.contents.map { it.id }
        val celebritiesByRestaurants = mapOf(
            restaurantIds[0] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
                .sampleList(2),
            restaurantIds[1] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                .sampleList(1),
        )
        When("회원이 최근 업데이트된 음식점 조회하면") {
            val memberId = 1L
            every { readRestaurantPort.readByCreatedAtBetween(any(), any(), any(), any()) } returns restaurants
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurants
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns listOf(
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, restaurants.contents[0])
                    .sample(),
            ) // 첫 번째 음식점만 관심 등록

            val latestRestaurants = restaurantQueryService.readWeeklyUpdateRestaurants(
                ReadWeeklyUpdateRestaurantsQuery(memberId, 0, 10),
            ).contents

            Then("관심 등록 여부가 포함되어 응답한다") {
                latestRestaurants.size shouldBe 2
                latestRestaurants[0].liked shouldBe true
                latestRestaurants[0].visitedCelebrities.size shouldBe 2
                latestRestaurants[1].liked shouldBe false
                latestRestaurants[1].visitedCelebrities.size shouldBe 1
            }
        }

        When("비회원이 최근 업데이트된 음식점 조회하면") {
            every { readRestaurantPort.readByCreatedAtBetween(any(), any(), any(), any()) } returns restaurants
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns celebritiesByRestaurants

            val latestRestaurants = restaurantQueryService.readWeeklyUpdateRestaurants(
                ReadWeeklyUpdateRestaurantsQuery(memberId = null, page = 0, size = 10),
            ).contents

            Then("관심 등록 여부는 false로 응답한다") {
                latestRestaurants.size shouldBe 2
                latestRestaurants.map { it.liked } shouldBe listOf(false, false)
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("주변 음식점 조회 시") {
        val restaurants = sut.giveMe<Restaurant>(3)
        val restaurantIds = restaurants.map { it.id }

        When("회원이 주변 음식점을 조회하면") {
            val memberId = 1L
            every { readRestaurantPort.readNearby(any()) } returns restaurants
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds) } returns mapOf(
                restaurantIds[0] to sut.giveMeBuilder<Celebrity>()
                    .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
                    .sampleList(2),
                restaurantIds[1] to sut.giveMeBuilder<Celebrity>()
                    .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                    .sampleList(2),
                restaurantIds[2] to sut.giveMeBuilder<Celebrity>()
                    .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
                    .sampleList(1),
            )
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns listOf(
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<InterestedRestaurant>()
                    .setExp(InterestedRestaurant::restaurant, restaurants[2])
                    .sample(),
            ) // 첫 번째, 세번째 음식점 관심 등록

            val readNearbyRestaurantsQuery = ReadNearbyRestaurantsQuery(
                memberId = memberId,
                restaurantId = 1L,
            )
            val nearbyRestaurants = restaurantQueryService.readNearbyRestaurants(readNearbyRestaurantsQuery)

            Then("관심 등록 여부가 포함되어 응답한다") {
                nearbyRestaurants.size shouldBe 3
                nearbyRestaurants[0].liked shouldBe true
                nearbyRestaurants[1].liked shouldBe false
                nearbyRestaurants[2].liked shouldBe true
            }
        }
    }

    Given("음식점 정보 조회시") {
        val restaurant = sut.giveMeOne(Restaurant::class.java)
        val restaurantId = restaurant.id
        val celebrities = sut.giveMeBuilder<Celebrity>()
            .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 1))
            .sampleList(2)
        When("회원이 음식점을 조회하면") {
            val memberId = 1L
            every { readRestaurantPort.readById(restaurant.id) } returns restaurant
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurant(restaurantId) } returns celebrities
            every { readInterestedRestaurantPort.existsInterestedRestaurant(memberId, restaurantId) } returns true

            val readRestaurantQuery = ReadRestaurantQuery(
                memberId = memberId,
                restaurantId = restaurantId,
            )
            val restaurantResult = restaurantQueryService.readRestaurantDetail(readRestaurantQuery)

            Then("관심 등록 여부가 포함되어 응답한다") {
                restaurantResult.liked shouldBe true
                restaurantResult.visitedCelebrities.size shouldBe 2
            }
        }

        When("비회원이 음식점을 조회하면") {
            every { readRestaurantPort.readById(restaurantId) } returns restaurant
            every { readCelebritiesPort.readVisitedCelebritiesByRestaurant(restaurantId) } returns celebrities
            val readRestaurantQuery = ReadRestaurantQuery(
                memberId = null,
                restaurantId = restaurantId,
            )
            val restaurantResult = restaurantQueryService.readRestaurantDetail(readRestaurantQuery)

            Then("관심 등록 여부는 false로 응답한다") {
                restaurantResult.liked shouldBe false
                restaurantResult.visitedCelebrities.size shouldBe 2
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("인기 음식점 조회시") {
        val restaurants = sut.giveMeBuilder<Restaurant>().sampleList(2)
        val restaurantIds = restaurants.map { it.id }
        val interestedRestaurants = listOf(
            sut.giveMeBuilder<InterestedRestaurant>()
                .setExp(InterestedRestaurant::restaurant, restaurants[0])
                .sample(),
        )

        When("일주일간 관심 추가가 가장 많은") {
            val memberId = 1L
            val query = ReadPopularRestaurantQuery(memberId = memberId)

            every { readRestaurantPort.readTop10InterestedRestaurantsInDate(any(), any()) } returns restaurants
            every {
                readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns interestedRestaurants

            val restaurantResult = restaurantQueryService.readPopularRestaurants(query)

            Then("음식점을 조회한다") {
                restaurantResult.size shouldBe 2
                restaurantResult[0].liked shouldBe true
                restaurantResult[1].liked shouldBe false
            }
        }
    }
}) {
    override suspend fun afterEach(
        testCase: TestCase,
        result: TestResult,
    ) {
        clearAllMocks()
        unmockkAll()
    }
}

private fun generateYoutubeContents(size: Int = 1): List<YoutubeContent> =
    sut.giveMeBuilder<YoutubeContent>().setInner(channelIdSpec).sampleList(size)
