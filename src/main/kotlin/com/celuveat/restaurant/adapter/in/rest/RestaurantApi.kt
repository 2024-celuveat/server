package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.common.adapter.out.rest.response.SliceResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "셀럽 API")
interface RestaurantApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 음식점 목록 조회")
    @GetMapping("/interested")
    fun getInterestedRestaurants(
        @AuthId memberId: Long,
        @Parameter(
            `in` = ParameterIn.QUERY,
            required = true,
            description = "조회할 페이지, default 0",
            example = "0",
        )
        @RequestParam("page") page: Int?,
        @Parameter(
            `in` = ParameterIn.QUERY,
            required = true,
            description = "조회할 크기, default 10",
            example = "10",
        )
        @RequestParam("size") size: Int?,
    ): SliceResponse<RestaurantPreviewResponse>
}
