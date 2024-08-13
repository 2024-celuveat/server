package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.ReadBestCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.ReadInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.BestCelebrityResult
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.`in`.result.SimpleCelebrityResult
import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.InterestedRestaurant
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.stereotype.Service

@Service
class CelebrityQueryService(
    private val readCelebritiesPort: ReadCelebritiesPort,
    private val readRestaurantPort: ReadRestaurantPort,
    private val readInterestedCelebritiesPort: ReadInterestedCelebritiesPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
) : ReadInterestedCelebritiesUseCase, ReadBestCelebritiesUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = readInterestedCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it.celebrity) }
    }

    override fun readBestCelebrities(memberId: Long?): List<BestCelebrityResult> {
        val bestCelebrities = readCelebritiesPort.findBestCelebrities()
        val restaurantsByCelebrity = bestCelebrities.associate {
            it.id to readRestaurantPort.findVisitedRestaurantByCelebrity(
                celebrityId = it.id,
                page = 0,
                size = 3
            ).contents
        }
        val interestedRestaurants = readInterestedRestaurants(memberId, restaurantsByCelebrity)

        return bestCelebrities.map { celebrity ->
            BestCelebrityResult(
                celebrity = SimpleCelebrityResult.from(celebrity),
                restaurants = restaurantsByCelebrity[celebrity.id]!!.map {
                    RestaurantPreviewResult.of(
                        restaurant = it,
                        liked = interestedRestaurants[it.id]?.let { true } ?: false
                    )
                }
            )
        }
    }

    private fun readInterestedRestaurants(
        memberId: Long?,
        restaurantsByCelebrity: Map<Long, List<Restaurant>>
    ): Map<Long, InterestedRestaurant> {
        val interestedRestaurants = memberId?.let {
            val restaurantIds = restaurantsByCelebrity.values.flatten().map { it.id }
            readInterestedRestaurantPort.findInterestedRestaurantsByIds(
                memberId = it,
                restaurantIds = restaurantIds
            ).associateBy { interested -> interested.restaurant.id }
        } ?: emptyMap()
        return interestedRestaurants
    }
}
