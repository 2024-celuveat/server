package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadRestaurantsRequest
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantDetailResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "음식점 API")
interface RestaurantApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 목록 조회")
    @GetMapping("/interested")
    fun getInterestedRestaurants(
        @Auth auth: AuthContext,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 개수 조회")
    @GetMapping("/interested/count")
    fun getAmountOfInterestedRestaurants(
        @Auth auth: AuthContext,
    ): Int

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 추가")
    @PostMapping("/interested/{restaurantId}")
    fun addInterestedRestaurant(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "음식점 ID",
            example = "1",
            required = true,
        )
        @PathVariable restaurantId: Long,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 삭제")
    @DeleteMapping("/interested/{restaurantId}")
    fun deleteInterestedRestaurant(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "음식점 ID",
            example = "1",
            required = true,
        )
        @PathVariable restaurantId: Long,
    )

    @Operation(summary = "셀럽이 다녀간 음식점 조회")
    @GetMapping("/celebrity/{celebrityId}")
    fun readCelebrityVisitedRestaurant(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "셀럽 ID",
            example = "1",
            required = true,
        )
        @PathVariable celebrityId: Long,
        @Parameter(
            `in` = ParameterIn.QUERY,
            description = "정렬 조건 (생략 가능), review, like, createdAt 중 하나. 기본값은 like",
            example = "review",
            required = false,
        )
        @RequestParam("sort", required = false, defaultValue = "like") sortCondition: String,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>

    @Operation(summary = "셀럽이 다녀간 음식점 개수 조회")
    @GetMapping("/celebrity/{celebrityId}/count")
    fun readAmountOfRestaurantsByCelebrity(
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "셀럽 ID",
            example = "1",
            required = true,
        )
        @PathVariable celebrityId: Long,
    ): Int

    @Operation(summary = "셀럽 추천 음식점 조회")
    @GetMapping("/celebrity/recommend")
    fun readCelebrityRecommendRestaurants(
        @Auth auth: AuthContext,
    ): List<RestaurantPreviewResponse>

    @Operation(summary = "음식점 조건 조회")
    @GetMapping
    fun readRestaurants(
        @Auth auth: AuthContext,
        @ModelAttribute request: ReadRestaurantsRequest,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>

    @Operation(summary = "음식점 개수 조회")
    @GetMapping("/count")
    fun readAmountOfRestaurants(
        @ModelAttribute request: ReadRestaurantsRequest,
    ): Int

    @Operation(summary = "이번주 업데이트된 음식점 조회")
    @GetMapping("/weekly")
    fun readWeeklyUpdatedRestaurants(
        @Auth auth: AuthContext,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>

    @Operation(summary = "이번주 업데이트된 음식점 개수 조회")
    @GetMapping("/weekly/count")
    fun readAmountOfWeeklyUpdatedRestaurants(
        @Auth auth: AuthContext,
    ): Int

    @Operation(summary = "주변 음식점 조회")
    @GetMapping("/nearby/{restaurantId}")
    fun readNearByRestaurants(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ): List<RestaurantPreviewResponse>

    @Operation(summary = "음식점 조회")
    @GetMapping("/{restaurantId}")
    fun readRestaurantDetail(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
    ): RestaurantDetailResponse

    @Operation(summary = "인기 음식점 조회")
    @GetMapping("/popular")
    fun readPopularRestaurants(
        @Auth auth: AuthContext,
    ): List<RestaurantPreviewResponse>
}
