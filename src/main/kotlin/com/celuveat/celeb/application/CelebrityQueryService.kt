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
        val celebrities = readInterestedCelebritiesPort.readInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it.celebrity) }
    }

    override fun readBestCelebrities(memberId: Long?): List<BestCelebrityResult> {
        val bestCelebrities = readCelebritiesPort.readBestCelebrities()
        val restaurantsByCelebrity = bestCelebrities.associate {
            it.id to readRestaurantPort.readVisitedRestaurantByCelebrity(
                celebrityId = it.id,
                page = 0,
                size = 3,
            ).contents
        }
        val interestedRestaurants = readInterestedRestaurants(memberId, restaurantsByCelebrity)
        return bestCelebrities.map { celebrity ->
            BestCelebrityResult(
                celebrity = SimpleCelebrityResult.from(celebrity),
                restaurants = restaurantsByCelebrity[celebrity.id]!!.map { restaurant ->
                    RestaurantPreviewResult.of(
                        restaurant = restaurant,
                        liked = interestedRestaurants.contains(restaurant),
                    )
                },
            )
        }
    }

    private fun readInterestedRestaurants(
        memberId: Long?,
        restaurantsByCelebrity: Map<Long, List<Restaurant>>,
    ): Set<Restaurant> {
        return memberId?.let {
            val restaurantIds = restaurantsByCelebrity.values.flatten().map { readRestaurant -> readRestaurant.id }
            readInterestedRestaurantPort.readInterestedRestaurantsByIds(
                memberId = it,
                restaurantIds = restaurantIds,
            ).map { interested -> interested.restaurant }.toSet()
        } ?: emptySet()
    }
}
