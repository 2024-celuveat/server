package com.celuveat.restaurant.application

import com.celuveat.common.utils.throwWhen
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.out.DeleteInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveInterestedRestaurantPort
import com.celuveat.restaurant.exception.AlreadyInterestedRestaurantException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val saveInterestedRestaurantPort: SaveInterestedRestaurantPort,
    private val deleteInterestedRestaurantPort: DeleteInterestedRestaurantPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
) : AddInterestedRestaurantsUseCase, DeleteInterestedRestaurantsUseCase {
    @Transactional
    override fun addInterestedRestaurant(command: AddInterestedRestaurantCommand) {
        throwWhen(
            readInterestedRestaurantPort.existsInterestedRestaurant(command.memberId, command.restaurantId)
        ) { AlreadyInterestedRestaurantException }
        saveInterestedRestaurantPort.saveInterestedRestaurant(
            memberId = command.memberId,
            restaurantId = command.restaurantId,
        )
    }

    @Transactional
    override fun deleteInterestedRestaurant(command: DeleteInterestedRestaurantCommand) {
        deleteInterestedRestaurantPort.deleteInterestedRestaurant(
            memberId = command.memberId,
            restaurantId = command.restaurantId,
        )
    }
}
