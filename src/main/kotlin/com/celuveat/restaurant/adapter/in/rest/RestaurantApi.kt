package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.Auth
import com.celuveat.auth.adaptor.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

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
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>

    @Operation(summary = "셀럽 추천 음식점 조회")
    @GetMapping("/celebrity/recommend")
    fun readCelebrityRecommendRestaurants(
        @Auth auth: AuthContext,
    ): List<RestaurantPreviewResponse>
}
