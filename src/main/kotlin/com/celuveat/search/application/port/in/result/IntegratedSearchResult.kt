package com.celuveat.search.application.port.`in`.result

import com.celuveat.celeb.domain.Celebrity
import com.celuveat.region.domain.Region
import com.celuveat.restaurant.domain.Restaurant

data class IntegratedSearchResult(
    val regionResults: List<ResultWithId>,
    val restaurantResults: List<ResultWithId>,
    val celebrityResults: List<ResultWithId>,
) {
    companion object {
        fun of(
            regions: List<Region>,
            restaurants: List<Restaurant>,
            celebrities: List<Celebrity>,
        ): IntegratedSearchResult {
            return IntegratedSearchResult(
                regionResults = regions.map { ResultWithId(id = it.id, name = it.name) },
                restaurantResults = restaurants.map { ResultWithId(id = it.id, name = it.name) },
                celebrityResults = celebrities.map { ResultWithId(id = it.id, name = it.name) },
            )
        }
    }
}

data class ResultWithId(
    val id: Long,
    val name: String,
)
