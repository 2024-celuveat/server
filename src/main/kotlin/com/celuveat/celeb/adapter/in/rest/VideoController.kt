package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.celeb.adapter.`in`.rest.response.VideoWithCelebrityResponse
import com.celuveat.celeb.application.port.`in`.ReadVideosByRestaurantUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/videos")
@RestController
class VideoController(
    private val readVideosByRestaurantUseCase: ReadVideosByRestaurantUseCase,
) : VideoApi {
    @GetMapping("/in/restaurants/{restaurantsId}")
    override fun readInterestedCelebrities(
        @PathVariable restaurantsId: Long,
    ): List<VideoWithCelebrityResponse> {
        val results = readVideosByRestaurantUseCase.readVideosByRestaurant(restaurantsId)
        return results.map { VideoWithCelebrityResponse.from(it) }
    }
}
