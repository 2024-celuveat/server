package com.celuveat.restaurant.application

import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.out.DeleteInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveInterestedRestaurantPort
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class RestaurantServiceTest : BehaviorSpec({
    val saveInterestedRestaurantPort: SaveInterestedRestaurantPort = mockk()
    val deleteInterestedRestaurantPort: DeleteInterestedRestaurantPort = mockk()

    val restaurantService = RestaurantService(
        saveInterestedRestaurantPort,
        deleteInterestedRestaurantPort,
    )

    Given("회원이 관심 음식점 추가 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("해당 음식점이") {
            every { saveInterestedRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = AddInterestedRestaurantCommand(memberId, restaurantId)
            restaurantService.addInterestedRestaurant(command)
            Then("관심 음식점으로 추가 된다") {
                verify { saveInterestedRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }

    Given("회원이 관심 음식점 삭제 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("해당 음식점이") {
            every { deleteInterestedRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = DeleteInterestedRestaurantCommand(memberId, restaurantId)
            restaurantService.deleteInterestedRestaurant(command)
            Then("관심 음식점에서 삭제 된다") {
                verify { deleteInterestedRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }
})
