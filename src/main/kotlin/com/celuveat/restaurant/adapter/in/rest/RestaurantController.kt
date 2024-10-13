package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadCelebrityVisitedRestaurantSortCondition
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadRestaurantsRequest
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantDetailResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadAmountOfInterestedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadAmountOfRestaurantByCelebrityUseCase
import com.celuveat.restaurant.application.port.`in`.ReadAmountOfRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadAmountOfWeeklyUpdateRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadNearbyRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadPopularRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantDetailUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadWeeklyUpdateRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.CountRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadPopularRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/restaurants")
@RestController
class RestaurantController(
    private val readInterestedRestaurantsUseCase: ReadInterestedRestaurantsUseCase,
    private val readAmountOfInterestedRestaurantUseCase: ReadAmountOfInterestedRestaurantUseCase,
    private val addInterestedRestaurantsUseCase: AddInterestedRestaurantsUseCase,
    private val deleteInterestedRestaurantsUseCase: DeleteInterestedRestaurantsUseCase,
    private val readCelebrityVisitedRestaurantUseCase: ReadCelebrityVisitedRestaurantUseCase,
    private val readCelebrityRecommendRestaurantsUseCase: ReadCelebrityRecommendRestaurantsUseCase,
    private val readRestaurantsUseCase: ReadRestaurantsUseCase,
    private val readAmountOfRestaurantsUseCase: ReadAmountOfRestaurantsUseCase,
    private val readWeeklyUpdateRestaurantsUseCase: ReadWeeklyUpdateRestaurantsUseCase,
    private val readAmountOfWeeklyUpdateRestaurantsUseCase: ReadAmountOfWeeklyUpdateRestaurantsUseCase,
    private val readNearbyRestaurantsUseCase: ReadNearbyRestaurantsUseCase,
    private val readRestaurantDetailUseCase: ReadRestaurantDetailUseCase,
    private val readPopularRestaurantsUseCase: ReadPopularRestaurantsUseCase,
    private val readAmountOfRestaurantByCelebrityUseCase: ReadAmountOfRestaurantByCelebrityUseCase,
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

    @GetMapping("/interested/count")
    override fun getAmountOfInterestedRestaurants(
        @Auth auth: AuthContext,
    ): Int {
        val memberId = auth.memberId()
        return readAmountOfInterestedRestaurantUseCase.readAmountOfInterestedRestaurant(memberId)
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
        @RequestParam("sort", required = false, defaultValue = "like") sortCondition: String,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse> {
        val sort = ReadCelebrityVisitedRestaurantSortCondition.from(sortCondition)
        val optionalMemberId = auth.optionalMemberId()
        val query = ReadCelebrityVisitedRestaurantQuery(
            memberId = optionalMemberId,
            celebrityId = celebrityId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
            sort = sort,
        )
        val visitedRestaurant = readCelebrityVisitedRestaurantUseCase.readCelebrityVisitedRestaurant(query)
        return SliceResponse.from(
            sliceResult = visitedRestaurant,
            converter = RestaurantPreviewResponse::from,
        )
    }

    @GetMapping("/celebrity/{celebrityId}/count")
    override fun readAmountOfRestaurantsByCelebrity(
        @PathVariable celebrityId: Long,
    ): Int {
        return readAmountOfRestaurantByCelebrityUseCase.readAmountOfRestaurantByCelebrity(celebrityId)
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

    @GetMapping("/count")
    override fun readAmountOfRestaurants(
        @ModelAttribute request: ReadRestaurantsRequest,
    ): Int {
        val query = CountRestaurantsQuery(
            category = request.category,
            region = request.region,
            searchArea = SquarePolygon.ofNullable(
                lowLongitude = request.lowLongitude,
                highLongitude = request.highLongitude,
                lowLatitude = request.lowLatitude,
                highLatitude = request.highLatitude,
            ),
            celebrityId = request.celebrityId,
        )
        return readAmountOfRestaurantsUseCase.readAmountOfRestaurants(query)
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

    override fun readAmountOfWeeklyUpdatedRestaurants(auth: AuthContext): Int {
        return readAmountOfWeeklyUpdateRestaurantsUseCase.readAmountOfWeeklyUpdateRestaurants()
    }

    @GetMapping("/nearby/{restaurantId}")
    override fun readNearByRestaurants(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ): List<RestaurantPreviewResponse> {
        val result = readNearbyRestaurantsUseCase.readNearbyRestaurants(
            ReadNearbyRestaurantsQuery(
                memberId = auth.optionalMemberId(),
                restaurantId = restaurantId,
            ),
        )
        return result.map(RestaurantPreviewResponse::from)
    }

    @GetMapping("/{restaurantId}")
    override fun readRestaurantDetail(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ): RestaurantDetailResponse {
        val result = readRestaurantDetailUseCase.readRestaurantDetail(
            ReadRestaurantQuery(
                memberId = auth.optionalMemberId(),
                restaurantId = restaurantId,
            ),
        )
        return RestaurantDetailResponse.from(result)
    }

    @GetMapping("/popular")
    override fun readPopularRestaurants(auth: AuthContext): List<RestaurantPreviewResponse> {
        val query = ReadPopularRestaurantQuery(memberId = auth.optionalMemberId())
        val results = readPopularRestaurantsUseCase.readPopularRestaurants(query)
        return results.map(RestaurantPreviewResponse::from)
    }
}
