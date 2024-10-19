package com.celuveat.region.adapter.`in`.rest

import com.celuveat.region.adapter.`in`.rest.response.RepresentativeRegionResponse
import com.celuveat.region.application.port.`in`.ReadRepresentativeRegionsUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/regions")
@RestController
class RegionController(
    private val readRepresentativeRegionsUseCase: ReadRepresentativeRegionsUseCase,
) : RegionApi {
    @GetMapping("/representative")
    override fun readRepresentativeRegions(): List<RepresentativeRegionResponse> {
        return readRepresentativeRegionsUseCase.readRepresentativeRegions()
            .map { RepresentativeRegionResponse.from(it) }
    }
}
