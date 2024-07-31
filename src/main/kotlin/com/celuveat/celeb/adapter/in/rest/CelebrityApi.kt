package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "셀럽 API")
interface CelebrityApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 셀럽 목록 조회")
    @GetMapping("/interested")
    fun getInterestedCelebrities(
        @AuthId memberId: Long,
    ): List<CelebrityResponse>
}
