package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.common.adapter.out.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/restaurants")
@RestController
class RestaurantController(
    private val getInterestedRestaurantsUseCase: GetInterestedRestaurantsUseCase,
) : RestaurantApi {
    @GetMapping("/interested")
    override fun getInterestedRestaurants(
        @AuthId memberId: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val interestedRestaurant = getInterestedRestaurantsUseCase.getInterestedRestaurant(
            GetInterestedRestaurantsQuery(
                memberId = memberId,
                page = pageable.pageNumber,
                size = pageable.pageSize,
            ),
        )
        return SliceResponse.from(
            sliceResult = interestedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }
}
