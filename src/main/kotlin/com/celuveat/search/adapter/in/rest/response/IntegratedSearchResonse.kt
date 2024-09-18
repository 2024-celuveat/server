package com.celuveat.search.adapter.`in`.rest.response

import com.celuveat.search.application.port.`in`.result.IntegratedSearchResult

data class IntegratedSearchResponse(
    val regionResults: List<ResponseWithId>,
    val restaurantResults: List<ResponseWithId>,
    val celebrityResults: List<ResponseWithId>,
) {
    companion object {
        fun from(result: IntegratedSearchResult): IntegratedSearchResponse {
            return IntegratedSearchResponse(
                regionResults = result.regionResults.map { ResponseWithId(id = it.id, name = it.name) },
                restaurantResults = result.restaurantResults.map { ResponseWithId(id = it.id, name = it.name) },
                celebrityResults = result.celebrityResults.map { ResponseWithId(id = it.id, name = it.name) },
            )
        }
    }
}

data class ResponseWithId(
    val id: Long,
    val name: String,
)
