package com.celuveat.sample.adapter.`in`.rest

import com.celuveat.sample.adapter.`in`.rest.request.SaveSampleRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "샘플 API")
interface SampleApi {

    @ApiResponses(
        value = [
            ApiResponse(description = "샘플 저장", responseCode = "201"),
            ApiResponse(responseCode = "400")
        ]
    )
    @Operation(summary = "샘플 저장")
    @GetMapping("/sample")
    fun sample(
        @RequestBody request: SaveSampleRequest,
    ): ResponseEntity<Long>
}
