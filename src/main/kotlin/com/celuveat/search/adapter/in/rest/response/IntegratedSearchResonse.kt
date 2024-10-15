package com.celuveat.search.adapter.`in`.rest.response

import com.celuveat.search.application.port.`in`.result.IntegratedSearchResult
import com.celuveat.search.application.port.`in`.result.RegionResult
import io.swagger.v3.oas.annotations.media.Schema

data class IntegratedSearchResponse(
    val regionResults: List<RegionResponse>,
    val restaurantResults: List<ResponseWithId>,
    val celebrityResults: List<ResponseWithId>,
    val categories: List<String>,
) {
    companion object {
        fun from(result: IntegratedSearchResult): IntegratedSearchResponse {
            return IntegratedSearchResponse(
                regionResults = result.regionResults.map { RegionResponse.from(it) },
                restaurantResults = result.restaurantResults.map { ResponseWithId(id = it.id, name = it.name) },
                celebrityResults = result.celebrityResults.map { ResponseWithId(id = it.id, name = it.name) },
                categories = result.categories,
            )
        }
    }
}

data class ResponseWithId(
    val id: Long,
    val name: String,
)

data class RegionResponse(
    val id: Long,
    val name: String,
    @Schema(
        description = "위도",
        example = "37.123456",
    )
    val latitude: Double,
    @Schema(
        description = "경도",
        example = "127.123456",
    )
    val longitude: Double,
) {
    companion object {
        fun from(result: RegionResult): RegionResponse {
            return RegionResponse(
                id = result.id,
                name = result.name,
                latitude = result.latitude,
                longitude = result.longitude,
            )
        }
    }
}
