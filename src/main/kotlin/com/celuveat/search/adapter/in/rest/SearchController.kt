package com.celuveat.search.adapter.`in`.rest

import com.celuveat.search.adapter.`in`.rest.response.IntegratedSearchResponse
import com.celuveat.search.application.port.`in`.IntegratedSearchUseCase
import com.celuveat.search.application.port.`in`.query.IntegratedSearchQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/search")
@RestController
class SearchController(
    private val integratedSearchUseCase: IntegratedSearchUseCase,
) : SearchApi {
    @GetMapping("/integrated")
    override fun integratedSearch(@RequestParam(name = "name") name: String): IntegratedSearchResponse {
        return IntegratedSearchResponse.from(integratedSearchUseCase.searchByName(IntegratedSearchQuery(name)))
    }
}
