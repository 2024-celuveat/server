package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.common.adapter.out.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/restaurants")
@RestController
class RestaurantController(
    private val getInterestedRestaurantsUseCase: GetInterestedRestaurantsUseCase,
) : RestaurantApi {

    @GetMapping("/interested")
    override fun getInterestedRestaurants(
        @AuthId memberId: Long,
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int?,
    ): SliceResponse<RestaurantPreviewResponse> {
        val interestedRestaurant = getInterestedRestaurantsUseCase.getInterestedRestaurant(
            GetInterestedRestaurantsQuery(
                memberId = memberId,
                page = page ?: 0,
                size = size ?: 10,
            )
        )
        return SliceResponse.from(
            sliceResult = interestedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }
}
