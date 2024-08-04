package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.domain.YoutubeContent
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
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

    Given("관심 음식점을 조회할 때") {
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
            val interestedRestaurants = restaurantsService.getInterestedRestaurant(getInterestedRestaurantsQuery)

            Then("관심 식당 목록을 반환한다") {
                interestedRestaurants.contents.size shouldBe 3
                interestedRestaurants.contents[0].visitedCelebrities.size shouldBe 2
                interestedRestaurants.hasNext shouldBe false
            }
        }
    }

    Given("회원이 관심 음식점 추가 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("해당 음식점이") {
            every { saveRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = AddInterestedRestaurantCommand(memberId, restaurantId)
            restaurantsService.addInterestedRestaurant(command)
            Then("관심 음식점으로 추가 된다") {
                verify { saveRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }

    Given("회원이 관심 음식점 삭제 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("해당 음식점이") {
            every { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = DeleteInterestedRestaurantCommand(memberId, restaurantId)
            restaurantsService.deleteInterestedRestaurant(command)
            Then("관심 음식점에서 삭제 된다") {
                verify { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }
})

private fun generateYoutubeContents(size: Int = 1): List<YoutubeContent> =
    sut.giveMeBuilder<YoutubeContent>().setInner(channelIdSpec).sampleList(size)
