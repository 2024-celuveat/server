package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.query.ReadCelebrityQuery
import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.InterestedCelebrity
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.member.domain.Member
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.InterestedRestaurant
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.into
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

class CelebrityQueryServiceTest : BehaviorSpec({
    val readCelebritiesPort: ReadCelebritiesPort = mockk()
    val readRestaurantPort: ReadRestaurantPort = mockk()
    val readInterestedCelebritiesPort: ReadInterestedCelebritiesPort = mockk()
    val readInterestedRestaurantPort: ReadInterestedRestaurantPort = mockk()

    val celebrityQueryService = CelebrityQueryService(
        readCelebritiesPort,
        readRestaurantPort,
        readInterestedCelebritiesPort,
        readInterestedRestaurantPort,
    )

    Given("관심 셀럽 조회 시") {
        val memberId = 1L
        val celebrityResult = sut.giveMeBuilder<InterestedCelebrity>()
            .setExp(InterestedCelebrity::member into Member::id, memberId)
            .setExp(InterestedCelebrity::celebrity into Celebrity::youtubeContents, generateYoutubeContents(1))
            .sampleList(3)

        When("관심 셀럽을 조회하면") {
            every { readInterestedCelebritiesPort.readInterestedCelebrities(memberId) } returns celebrityResult
            val result = celebrityQueryService.getInterestedCelebrities(memberId)

            Then("셀럽이 조회된다") {
                result.size shouldBe 3
            }
        }
    }

    Given("인기 셀럽 조회 시") {
        val memberId = 1L
        val celebrityResult = sut.giveMeBuilder<Celebrity>()
            .setExp(Celebrity::youtubeContents, generateYoutubeContents(1))
            .sampleList(2)
        val restaurantsA = sut.giveMe<Restaurant>(2)
        val restaurantsB = sut.giveMe<Restaurant>(3)
        val interestedRestaurants = restaurantsA.map {
            sut.giveMeBuilder<InterestedRestaurant>()
                .setExp(InterestedRestaurant::member into Member::id, memberId)
                .setExp(InterestedRestaurant::restaurant, it)
                .sample()
        }

        When("회원이 조회하면") {
            every { readCelebritiesPort.readBestCelebrities() } returns celebrityResult
            every { readRestaurantPort.readVisitedRestaurantByCelebrity(any(), any(), any()) } returns
                    SliceResult.of(restaurantsA, 0, false) andThen
                    SliceResult.of(restaurantsB, 0, false)
            every { readInterestedRestaurantPort.readInterestedRestaurantsByIds(memberId, any()) } returns
                    interestedRestaurants

            val result = celebrityQueryService.readBestCelebrities(memberId)

            Then("음식점도 좋아요 여부와 함께 조회 된다") {
                result.size shouldBe 2
                result[0].restaurants.forAll { it.liked shouldBe true }
                result[1].restaurants.size shouldBe 3
            }
        }

        When("비회원이 조회하면") {
            every { readCelebritiesPort.readBestCelebrities() } returns celebrityResult
            every { readRestaurantPort.readVisitedRestaurantByCelebrity(any(), any(), any()) } returns
                    SliceResult.of(restaurantsA, 0, false) andThen
                    SliceResult.of(restaurantsB, 0, false)

            val result = celebrityQueryService.readBestCelebrities(null)
            Then("좋아요 여부 없이 음식점도 함께 조회 된다") {
                result.size shouldBe 2
                result[0].restaurants.forAll { it.liked shouldBe false }
                result[1].restaurants.forAll { it.liked shouldBe false }
                verify { readInterestedRestaurantPort wasNot Called }
            }
        }
    }

    Given("단일 셀럽 조회 시") {
        val celebrity = sut.giveMeBuilder<Celebrity>()
            .setExp(Celebrity::youtubeContents, generateYoutubeContents(size = 2))
            .sample()

        When("회원이 셀럽을 조회하면") {
            val memberId = 1L
            every { readCelebritiesPort.readById(celebrity.id) } returns celebrity
            every { readInterestedCelebritiesPort.existsInterestedCelebrity(celebrity.id, memberId) } returns true

            val query = ReadCelebrityQuery(memberId, celebrity.id)
            val result = celebrityQueryService.readCelebrity(query)

            Then("셀럽과 관심 여부가 조회된다") {
                result.celebrity.id shouldBe celebrity.id
                result.interested shouldBe true
            }
        }

        When("비회원이 셀럽을 조회하면") {
            every { readCelebritiesPort.readById(celebrity.id) } returns celebrity

            val query = ReadCelebrityQuery(null, celebrity.id)
            val result = celebrityQueryService.readCelebrity(query)

            Then("셀럽만 조회된다") {
                result.celebrity.id shouldBe celebrity.id
                result.interested shouldBe false
                verify { readInterestedCelebritiesPort wasNot Called }
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
