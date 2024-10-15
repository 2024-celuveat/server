package com.celuveat.search.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.region.application.port.out.ReadRegionPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.search.application.port.`in`.IntegratedSearchUseCase
import com.celuveat.search.application.port.`in`.query.IntegratedSearchQuery
import com.celuveat.search.application.port.`in`.result.IntegratedSearchResult
import org.springframework.stereotype.Service

@Service
class SearchQueryService(
    private val readRegionPort: ReadRegionPort,
    private val readRestaurantPort: ReadRestaurantPort,
    private val readCelebritiesPort: ReadCelebritiesPort,
) : IntegratedSearchUseCase {
    override fun searchByName(query: IntegratedSearchQuery): IntegratedSearchResult {
        val regions = readRegionPort.readByName(query.name)
        val restaurants = readRestaurantPort.readByName(query.name)
        val celebrities = readCelebritiesPort.readByName(query.name)
        val categories = readRestaurantPort.readCategoriesByKeyword(query.name)
        return IntegratedSearchResult.of(
            regions = regions,
            restaurants = restaurants,
            celebrities = celebrities,
            categories = categories,
        )
    }
}
