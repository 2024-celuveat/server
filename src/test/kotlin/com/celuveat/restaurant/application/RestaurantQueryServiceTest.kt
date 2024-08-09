package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.out.FindInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import com.celuveat.restaurant.domain.InterestedRestaurant
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify

class RestaurantQueryServiceTest : BehaviorSpec({
    val findRestaurantPort: FindRestaurantPort = mockk()
    val findCelebritiesPort: FindCelebritiesPort = mockk()
    val findInterestedRestaurantPort: FindInterestedRestaurantPort = mockk()

    val restaurantQueryService = RestaurantQueryService(
        findRestaurantPort,
        findCelebritiesPort,
        findInterestedRestaurantPort,
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
            findInterestedRestaurantPort.findInterestedRestaurants(
                memberId,
                page,
                size,
            )
        } returns interestedRestaurantResult
        every { findCelebritiesPort.findVisitedCelebritiesByRestaurants(interestedRestaurantResultIds) } returns mapOf(
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
            val getInterestedRestaurantsQuery = GetInterestedRestaurantsQuery(
                memberId = memberId,
                page = page,
                size = size,
            )
            val interestedRestaurants = restaurantQueryService.getInterestedRestaurant(getInterestedRestaurantsQuery)

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
                findRestaurantPort.findVisitedRestaurantByCelebrity(
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
                findInterestedRestaurantPort.findInterestedRestaurantsByIds(
                    memberId,
                    restaurantIds,
                )
            } returns interestedRestaurants
            val readVisitedRestaurantQuery = ReadVisitedRestaurantQuery(
                memberId = memberId,
                celebrityId = celebrityId,
                page = page,
                size = size,
            )
            val visitedRestaurants = restaurantQueryService.readVisitedRestaurant(readVisitedRestaurantQuery)

            Then("관심 등록 여부가 포함되어 응답한다") {
                visitedRestaurants.contents.size shouldBe 2
                visitedRestaurants.contents[0].liked shouldBe true
            }
        }

        When("비회원이 음식점을 조회하면") {
            every {
                findRestaurantPort.findVisitedRestaurantByCelebrity(
                    celebrityId,
                    page,
                    size,
                )
            } returns visitedRestaurantResult
            val readVisitedRestaurantQuery = ReadVisitedRestaurantQuery(
                memberId = null,
                celebrityId = celebrityId,
                page = page,
                size = size,
            )
            val visitedRestaurants = restaurantQueryService.readVisitedRestaurant(readVisitedRestaurantQuery)

            Then("관심 등록 여부는 false로 응답한다") {
                visitedRestaurants.contents.size shouldBe 2
                visitedRestaurants.contents.map { it.liked } shouldBe listOf(false, false)
                verify { findInterestedRestaurantPort wasNot Called }
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
