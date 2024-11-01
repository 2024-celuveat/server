package com.celuveat.region.adapter.`in`.rest.response

import com.celuveat.region.application.port.`in`.result.RepresentativeRegionResult

data class RepresentativeRegionResponse(
    val name: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        fun from(result: RepresentativeRegionResult): RepresentativeRegionResponse {
            return RepresentativeRegionResponse(
                name = result.name,
                imageUrl = result.imageUrl,
                latitude = result.latitude,
                longitude = result.longitude,
            )
        }
    }
}
