package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.common.adapter.out.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "음식점 API")
interface RestaurantApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 목록 조회")
    @Parameters(
        Parameter(name = "page", description = "페이지 번호", example = "0", required = true),
        Parameter(name = "size", description = "페이지 크기", example = "10", required = true),
    )
    @GetMapping("/interested")
    fun getInterestedRestaurants(
        @AuthId memberId: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<RestaurantPreviewResponse>
}
