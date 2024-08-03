package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeChannel
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.command.ToggleInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.out.DeleteRestaurantPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.support.channelIdSpec
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class RestaurantsServiceTest : BehaviorSpec({

    val findRestaurantPort: FindRestaurantPort = mockk()
    val findCelebritiesPort: FindCelebritiesPort = mockk()
    val saveRestaurantPort: SaveRestaurantPort = mockk()
    val deleteRestaurantPort: DeleteRestaurantPort = mockk()

    val restaurantsService = RestaurantsService(
        findRestaurantPort,
        findCelebritiesPort,
        saveRestaurantPort,
        deleteRestaurantPort,
    )

    Given("관심 식당을 조회할 때") {
        val memberId = 1L
        val page = 0
        val size = 2
        val interestedRestaurantResult = SliceResult.of(
            contents = sut.giveMeBuilder<Restaurant>().sampleList(3),
            currentPage = 0,
            hasNext = false,
        )
        val interestedRestaurantResultIds = interestedRestaurantResult.contents.map { it.id }
        every { findRestaurantPort.findInterestedRestaurants(memberId, page, size) } returns interestedRestaurantResult
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

    Given("관심 식당 추가/삭제 토글 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("이미 추가된 식당인 경우") {
            val restaurant = sut.giveMeBuilder<Restaurant>().sample()
            every { findRestaurantPort.findInterestedRestaurantOrNull(memberId, restaurantId) } returns restaurant
            every { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = ToggleInterestedRestaurantCommand(memberId, restaurantId)
            restaurantsService.toggleInterestedRestaurant(command)
            Then("관심 식당이 삭제 된다") {
                verify { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            }
        }

        When("추가 되지 않은 식당인 경우") {
            every { findRestaurantPort.findInterestedRestaurantOrNull(memberId, restaurantId) } returns null
            every { saveRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = ToggleInterestedRestaurantCommand(memberId, restaurantId)
            restaurantsService.toggleInterestedRestaurant(command)
            Then("관심 식당이 삭제된다") {
                verify { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }
})

private fun generateYoutubeChannels(size: Int = 1): List<YoutubeChannel> =
    sut.giveMeBuilder<YoutubeChannel>().setInner(channelIdSpec).sampleList(size)
