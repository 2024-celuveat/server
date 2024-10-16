package com.celuveat.region.adapter.`in`.rest

import com.celuveat.region.adapter.`in`.rest.response.RepresentativeRegionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "지역 API")
interface RegionApi {
    @Operation(summary = "대표 지역 조회")
    @GetMapping("/regions/representative")
    fun readRepresentativeRegions(): List<RepresentativeRegionResponse>
}
