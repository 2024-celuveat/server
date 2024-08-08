package com.celuveat.restaurant.application

import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.out.DeleteRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveRestaurantPort
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class RestaurantServiceTest : BehaviorSpec({
    val saveRestaurantPort: SaveRestaurantPort = mockk()
    val deleteRestaurantPort: DeleteRestaurantPort = mockk()

    val restaurantService = RestaurantService(
        saveRestaurantPort,
        deleteRestaurantPort,
    )

    Given("회원이 관심 음식점 추가 시") {
        val memberId = 1L
        val restaurantId = 1L

        When("해당 음식점이") {
            every { saveRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) } returns Unit

            val command = AddInterestedRestaurantCommand(memberId, restaurantId)
            restaurantService.addInterestedRestaurant(command)
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
            restaurantService.deleteInterestedRestaurant(command)
            Then("관심 음식점에서 삭제 된다") {
                verify { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            }
        }
    }
})
