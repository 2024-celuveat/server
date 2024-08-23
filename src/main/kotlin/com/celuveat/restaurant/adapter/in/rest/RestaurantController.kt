package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadRestaurantsRequest
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadWeeklyUpdateRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
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
    private val readCelebrityVisitedRestaurantUseCase: ReadCelebrityVisitedRestaurantUseCase,
    private val readCelebrityRecommendRestaurantsUseCase: ReadCelebrityRecommendRestaurantsUseCase,
    private val readRestaurantsUseCase: ReadRestaurantsUseCase,
    private val readWeeklyUpdateRestaurantsUseCase: ReadWeeklyUpdateRestaurantsUseCase,
) : RestaurantApi {
    @GetMapping("/interested")
    override fun getInterestedRestaurants(
        @Auth auth: AuthContext,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val memberId = auth.memberId()
        val query = ReadInterestedRestaurantsQuery(
            memberId = memberId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        val interestedRestaurant = readInterestedRestaurantsUseCase.readInterestedRestaurant(query)
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
        val query = ReadCelebrityVisitedRestaurantQuery(
            memberId = optionalMemberId,
            celebrityId = celebrityId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        val visitedRestaurant = readCelebrityVisitedRestaurantUseCase.readCelebrityVisitedRestaurant(query)
        return SliceResponse.from(
            sliceResult = visitedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }

    @GetMapping("/celebrity/recommend")
    override fun readCelebrityRecommendRestaurants(
        @Auth auth: AuthContext,
    ): List<RestaurantPreviewResponse> {
        val memberId = auth.optionalMemberId()
        val query = ReadCelebrityRecommendRestaurantsQuery(memberId = memberId)
        val interestedRestaurant = readCelebrityRecommendRestaurantsUseCase.readCelebrityRecommendRestaurants(query)
        return interestedRestaurant.map(RestaurantPreviewResponse::from)
    }

    @GetMapping
    override fun readRestaurants(
        @Auth auth: AuthContext,
        @ModelAttribute request: ReadRestaurantsRequest,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val query = request.toQuery(
            memberId = auth.optionalMemberId(),
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        val result = readRestaurantsUseCase.readRestaurants(query)
        return SliceResponse.from(
            sliceResult = result,
            converter = RestaurantPreviewResponse::from,
        )
    }

    @GetMapping("/weekly")
    override fun readWeeklyUpdatedRestaurants(
        @Auth auth: AuthContext,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val result = readWeeklyUpdateRestaurantsUseCase.readWeeklyUpdateRestaurants(
            ReadWeeklyUpdateRestaurantsQuery(
                auth.optionalMemberId(),
                page = pageable.pageNumber,
                size = pageable.pageSize,
            ),
        )
        return SliceResponse.from(
            sliceResult = result,
            converter = RestaurantPreviewResponse::from,
        )
    }
}
