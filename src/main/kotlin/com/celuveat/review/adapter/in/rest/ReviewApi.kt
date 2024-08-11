package com.celuveat.review.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.Auth
import com.celuveat.auth.adaptor.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.review.adapter.`in`.rest.request.UpdateReviewRequest
import com.celuveat.review.adapter.`in`.rest.request.WriteReviewRequest
import com.celuveat.review.adapter.`in`.rest.response.ReviewPreviewResponse
import com.celuveat.review.adapter.`in`.rest.response.SingleReviewResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "리뷰 API")
interface ReviewApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "리뷰 작성")
    @PostMapping
    fun writeReview(
        @Auth auth: AuthContext,
        @RequestBody request: WriteReviewRequest,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    fun updateReview(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "리뷰 ID",
            example = "1",
            required = true,
        )
        @PathVariable reviewId: Long,
        @RequestBody request: UpdateReviewRequest,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "리뷰 ID",
            example = "1",
            required = true,
        )
        @PathVariable reviewId: Long,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "리뷰 도움돼요")
    @PostMapping("/helpful/{reviewId}")
    fun clickHelpfulReview(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "리뷰 ID",
            example = "1",
            required = true,
        )
        @PathVariable reviewId: Long,
    )

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "리뷰 도움돼요 삭제")
    @DeleteMapping("/helpful/{reviewId}")
    fun deleteHelpfulReview(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "리뷰 ID",
            example = "1",
            required = true,
        )
        @PathVariable reviewId: Long,
    )

    @Operation(summary = "특정 음식점의 리뷰 전체 조회")
    @GetMapping("/restaurants/{restaurantId}")
    @Parameters(
        Parameter(name = "page", description = "페이지 번호", example = "0", required = true),
        Parameter(name = "size", description = "페이지 크기", example = "10", required = true),
    )
    fun readAllRestaurantsReviews(
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "음식점 ID",
            example = "1",
            required = true,
        )
        @PathVariable restaurantId: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<ReviewPreviewResponse>

    @Operation(summary = "리뷰 상세조회")
    @GetMapping("/{reviewId}")
    fun readReview(
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "리뷰 ID",
            example = "1",
            required = true,
        )
        @PathVariable reviewId: Long,
    ): SingleReviewResponse
}
