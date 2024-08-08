package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Tag(name = "셀럽 API")
interface CelebrityApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 셀럽 목록 조회")
    @GetMapping("/interested")
    fun readInterestedCelebrities(
        @AuthId memberId: Long,
    ): List<CelebrityResponse>

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 셀럽 추가")
    @PostMapping("/interested/{celebrityId}")
    fun addInterestedCelebrity(
        @AuthId memberId: Long,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "셀럽 ID",
            example = "1",
            required = true,
        )
        @PathVariable celebrityId: Long,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "관심 셀럽 삭제")
    @DeleteMapping("/interested/{celebrityId}")
    fun deleteInterestedCelebrity(
        @AuthId memberId: Long,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "셀럽 ID",
            example = "1",
            required = true,
        )
        @PathVariable celebrityId: Long,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "인기 셀럽 조회")
    @GetMapping("/interested")
    fun readBestCelebrities(): List<SimpleCelebrityResponse>
}
