package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeChannel
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.out.FindRestaurantsPort
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RestaurantsServiceTest : BehaviorSpec({

    val findRestaurantsPort: FindRestaurantsPort = mockk()
    val findCelebritiesPort: FindCelebritiesPort = mockk()

    val restaurantsService = RestaurantsService(
        findRestaurantsPort,
        findCelebritiesPort,
    )

    Given("관심 식당을 조회할 때") {
        val memberId = 1L
        val page = 0
        val size = 2
        val interestedRestaurantResult = SliceResult.of(
            contents = sut.giveMeBuilder<Restaurant>().sampleList(3),
            currentPage = 0,
            hasNext = false
        )
        val interestedRestaurantResultIds = interestedRestaurantResult.contents.map { it.id }
        every { findRestaurantsPort.findInterestedRestaurants(memberId, page, size) } returns interestedRestaurantResult
        every { findCelebritiesPort.findVisitedCelebritiesByRestaurants(interestedRestaurantResultIds) } returns mapOf(
            interestedRestaurantResultIds[0] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeChannels, generateYoutubeChannels(size = 2))
                .sampleList(2),
            interestedRestaurantResultIds[1] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeChannels, generateYoutubeChannels(size = 1))
                .sampleList(2),
            interestedRestaurantResultIds[2] to sut.giveMeBuilder<Celebrity>()
                .setExp(Celebrity::youtubeChannels, generateYoutubeChannels(size = 1))
                .sampleList(1),
        )
        When("회원이 관심 식당을 조회하면") {
            val getInterestedRestaurantsQuery = GetInterestedRestaurantsQuery(
                memberId = memberId,
                page = page,
                size = size,
            )
            val interestedRestaurants = restaurantsService.getInterestedRestaurant(getInterestedRestaurantsQuery)

            Then("관심 식당 목록을 반환한다") {
                interestedRestaurants.contents.size shouldBe 3
                interestedRestaurants.contents[0].visitedCelebrities.size shouldBe 2
                interestedRestaurants.hasNext shouldBe false
            }
        }
    }
})

private fun generateYoutubeChannels(size: Int = 1): List<YoutubeChannel> =
    sut.giveMeBuilder<YoutubeChannel>().setInner(channelIdSpec).sampleList(size)
