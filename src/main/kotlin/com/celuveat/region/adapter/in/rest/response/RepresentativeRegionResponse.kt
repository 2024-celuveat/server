package com.celuveat.region.adapter.`in`.rest.response

import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult

data class RepresentativeRegionResponse(
    val name: String,
    val imageUrl: String,
) {
    companion object {
        fun from(result: RepresentativeRegionResult): RepresentativeRegionResponse {
            return RepresentativeRegionResponse(
                name = result.name,
                imageUrl = result.imageUrl,
            )
        }
    }
}
