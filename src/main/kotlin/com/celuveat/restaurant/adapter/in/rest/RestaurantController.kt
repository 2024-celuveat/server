package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.Auth
import com.celuveat.auth.adaptor.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadVisitedRestaurantQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/restaurants")
@RestController
class RestaurantController(
    private val readInterestedRestaurantsUseCase: ReadInterestedRestaurantsUseCase,
    private val addInterestedRestaurantsUseCase: AddInterestedRestaurantsUseCase,
    private val deleteInterestedRestaurantsUseCase: DeleteInterestedRestaurantsUseCase,
    private val readVisitedRestaurantUseCase: ReadVisitedRestaurantUseCase,
) : RestaurantApi {
    @GetMapping("/interested")
    override fun getInterestedRestaurants(
        @Auth auth: AuthContext,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val memberId = auth.memberId()
        val query = GetInterestedRestaurantsQuery(
            memberId = memberId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        val interestedRestaurant = readInterestedRestaurantsUseCase.getInterestedRestaurant(query)
        return SliceResponse.from(
            sliceResult = interestedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }

    @PostMapping("/interested/{restaurantId}")
    override fun addInterestedRestaurant(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ) {
        val memberId = auth.memberId()
        val command = AddInterestedRestaurantCommand(
            memberId = memberId,
            restaurantId = restaurantId,
        )
        addInterestedRestaurantsUseCase.addInterestedRestaurant(command)
    }

    @DeleteMapping("/interested/{restaurantId}")
    override fun deleteInterestedRestaurant(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ) {
        val memberId = auth.memberId()
        val command = DeleteInterestedRestaurantCommand(
            memberId = memberId,
            restaurantId = restaurantId,
        )
        deleteInterestedRestaurantsUseCase.deleteInterestedRestaurant(command)
    }

    @GetMapping("/celebrity/{celebrityId}")
    override fun readCelebrityVisitedRestaurant(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val optionalMemberId = auth.optionalMemberId()
        val query = ReadVisitedRestaurantQuery(
            memberId = optionalMemberId,
            celebrityId = celebrityId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        val visitedRestaurant = readVisitedRestaurantUseCase.readVisitedRestaurant(query)
        return SliceResponse.from(
            sliceResult = visitedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }
}
