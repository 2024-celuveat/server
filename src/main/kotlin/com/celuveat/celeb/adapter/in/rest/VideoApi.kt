package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.celeb.adapter.`in`.rest.response.VideoWithCelebrityResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "영상 API")
interface VideoApi {
    @Operation(summary = "음식점이 나온 영상 조회")
    @GetMapping("/in/restaurants/{restaurantsId}")
    fun readInterestedCelebrities(
        @Parameter(
            name = "restaurantsId",
            description = "음식점 ID",
            required = true,
            `in` = ParameterIn.PATH,
        ) @PathVariable restaurantsId: Long,
    ): List<VideoWithCelebrityResponse>
}
