package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ToggleInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.ToggleInterestedRestaurantCommand
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
) : GetInterestedRestaurantsUseCase, ToggleInterestedRestaurantsUseCase {
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
    override fun toggleInterestedRestaurant(command: ToggleInterestedRestaurantCommand) {
        val memberId = command.memberId
        val restaurantId = command.restaurantId
        findRestaurantPort.findInterestedRestaurantOrNull(memberId, restaurantId)
            ?.let { deleteRestaurantPort.deleteInterestedRestaurant(memberId, restaurantId) }
            ?: run { saveRestaurantPort.saveInterestedRestaurant(memberId, restaurantId) }
    }
}
