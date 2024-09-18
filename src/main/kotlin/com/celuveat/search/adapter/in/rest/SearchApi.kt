package com.celuveat.search.adapter.`in`.rest

import com.celuveat.search.adapter.`in`.rest.response.IntegratedSearchResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "검색 API")
interface SearchApi {

    @Operation(summary = "지역, 셀럽, 음식점 통합 검색")
    @GetMapping("/integrated")
    fun integratedSearch(
        @Parameter(
            `in` = ParameterIn.QUERY,
            description = "이름",
            example = "감자",
            required = true,
        )
        @RequestParam(name = "name") name: String,
    ): IntegratedSearchResponse
}
