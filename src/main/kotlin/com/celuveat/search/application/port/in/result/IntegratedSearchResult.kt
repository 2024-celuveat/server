package com.celuveat.search.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.region.domain.Region
import com.celuveat.restaurant.domain.Restaurant
import io.swagger.v3.oas.annotations.media.Schema

data class IntegratedSearchResult(
    val regionResults: List<RegionResult>,
    val restaurantResults: List<ResultWithId>,
    val celebrityResults: List<ResultWithId>,
    val categories: List<String>,
) {
    companion object {
        fun of(
            regions: List<Region>,
            restaurants: List<Restaurant>,
            celebrities: List<Celebrity>,
            categories: List<String>,
        ): IntegratedSearchResult {
            return IntegratedSearchResult(
                regionResults = regions.map { RegionResult.from(it) },
                restaurantResults = restaurants.map { ResultWithId(id = it.id, name = it.name) },
                celebrityResults = celebrities.map { ResultWithId(id = it.id, name = it.name) },
                categories = categories,
            )
        }
    }
}

data class ResultWithId(
    val id: Long,
    val name: String,
)

data class RegionResult(
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
        fun from(result: Region): RegionResult {
            return RegionResult(
                id = result.id,
                name = result.name,
                latitude = result.latitude,
                longitude = result.longitude,
            )
        }
    }
}
