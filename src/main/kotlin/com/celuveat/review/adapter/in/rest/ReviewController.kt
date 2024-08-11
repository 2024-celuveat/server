package com.celuveat.review.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.common.adapter.out.rest.response.SliceResponse
import com.celuveat.review.adapter.`in`.rest.request.UpdateReviewRequest
import com.celuveat.review.adapter.`in`.rest.request.WriteReviewRequest
import com.celuveat.review.adapter.`in`.rest.response.ReviewPreviewResponse
import com.celuveat.review.adapter.`in`.rest.response.SingleReviewResponse
import com.celuveat.review.application.port.`in`.ClickHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteReviewUseCase
import com.celuveat.review.application.port.`in`.GetReviewListUseCase
import com.celuveat.review.application.port.`in`.GetSingleReviewUseCase
import com.celuveat.review.application.port.`in`.UpdateReviewUseCase
import com.celuveat.review.application.port.`in`.WriteReviewUseCase
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/reviews")
@RestController
class ReviewController(
    private val writeReviewUseCase: WriteReviewUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val clickHelpfulReviewUseCase: ClickHelpfulReviewUseCase,
    private val deleteHelpfulReviewUseCase: DeleteHelpfulReviewUseCase,
    private val getReviewListUseCase: GetReviewListUseCase,
    private val getSingleReviewUseCase: GetSingleReviewUseCase,
) : ReviewApi {

    @PostMapping
    override fun writeReview(
        @AuthId memberId: Long,
        @Valid @RequestBody request: WriteReviewRequest
    ) {
        writeReviewUseCase.write(request.toCommand(memberId))
    }

    @PutMapping("/{reviewId}")
    override fun updateReview(
        @AuthId memberId: Long,
        @PathVariable reviewId: Long,
        @RequestBody request: UpdateReviewRequest,
    ) {
        updateReviewUseCase.update(request.toCommand(reviewId = reviewId, memberId = memberId))
    }

    @DeleteMapping("/{reviewId}")
    override fun deleteReview(
        @AuthId memberId: Long,
        @PathVariable reviewId: Long,
    ) {
        deleteReviewUseCase.delete(memberId = memberId, reviewId = reviewId)
    }

    @PostMapping("/help/{reviewId}")
    override fun clickHelpfulReview(
        @AuthId memberId: Long,
        @PathVariable reviewId: Long,
    ) {
        clickHelpfulReviewUseCase.clickHelpfulReview(memberId = memberId, reviewId = reviewId)
    }

    @DeleteMapping("/help/{reviewId}")
    override fun deleteHelpfulReview(memberId: Long, reviewId: Long) {
        deleteHelpfulReviewUseCase.deleteHelpfulReview(memberId = memberId, reviewId = reviewId)
    }

    @GetMapping("/restaurants/{restaurantId}")
    override fun readAllRestaurantsReviews(
        @PathVariable restaurantId: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<ReviewPreviewResponse> {
        val reviews = getReviewListUseCase.getAll(
            restaurantId = restaurantId,
            page = pageable.pageNumber,
            size = pageable.pageSize,
        )
        return SliceResponse.from(
            sliceResult = reviews,
            converter = ReviewPreviewResponse::from,
        )
    }

    @GetMapping("/{reviewId}")
    override fun readReview(
        @PathVariable reviewId: Long
    ): SingleReviewResponse {
        return SingleReviewResponse.from(getSingleReviewUseCase.get(id = reviewId))
    }
}
