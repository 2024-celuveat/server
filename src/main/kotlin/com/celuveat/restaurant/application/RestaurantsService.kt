package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.DeleteRestaurantPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveRestaurantPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantsService(
    private val findRestaurantPort: FindRestaurantPort,
    private val findCelebritiesPort: FindCelebritiesPort,
    private val saveRestaurantPort: SaveRestaurantPort,
    private val deleteRestaurantPort: DeleteRestaurantPort,
) : GetInterestedRestaurantsUseCase, AddInterestedRestaurantsUseCase, DeleteInterestedRestaurantsUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = findRestaurantPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = findCelebritiesPort.findVisitedCelebritiesByRestaurants(
            interestedRestaurants.contents.map { it.id },
        )
        return interestedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = true,
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    @Transactional
    override fun addInterestedRestaurant(command: AddInterestedRestaurantCommand) {
        saveRestaurantPort.saveInterestedRestaurant(
            memberId = command.memberId,
            restaurantId = command.restaurantId,
        )
    }

    @Transactional
    override fun deleteInterestedRestaurant(command: DeleteInterestedRestaurantCommand) {
        deleteRestaurantPort.deleteInterestedRestaurant(
            memberId = command.memberId,
            restaurantId = command.restaurantId,
        )
    }
}
