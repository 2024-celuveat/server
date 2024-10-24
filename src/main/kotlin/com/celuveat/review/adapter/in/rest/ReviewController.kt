package com.celuveat.review.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.review.adapter.`in`.rest.request.ReadReviewSortCondition
import com.celuveat.review.adapter.`in`.rest.request.UpdateReviewRequest
import com.celuveat.review.adapter.`in`.rest.request.WriteReviewRequest
import com.celuveat.review.adapter.`in`.rest.response.ReviewPreviewResponse
import com.celuveat.review.adapter.`in`.rest.response.SingleReviewResponse
import com.celuveat.review.application.port.`in`.ClickHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteReviewUseCase
import com.celuveat.review.application.port.`in`.ReadAmountOfRestaurantReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadMyReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadRestaurantReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadSingleReviewUseCase
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// TODO test
@RequestMapping("/reviews")
@RestController
class ReviewController(
    private val writeReviewUseCase: WriteReviewUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val clickHelpfulReviewUseCase: ClickHelpfulReviewUseCase,
    private val deleteHelpfulReviewUseCase: DeleteHelpfulReviewUseCase,
    private val readRestaurantReviewsUseCase: ReadRestaurantReviewsUseCase,
    private val readAmountOfRestaurantReviewsUseCase: ReadAmountOfRestaurantReviewsUseCase,
    private val readSingleReviewUseCase: ReadSingleReviewUseCase,
    private val readMyReviewsUseCase: ReadMyReviewsUseCase,
) : ReviewApi {
    @PostMapping
    override fun writeReview(
        @Auth auth: AuthContext,
        @Valid @RequestBody request: WriteReviewRequest,
    ) {
        val memberId = auth.memberId()
        writeReviewUseCase.write(request.toCommand(memberId))
    }

    @PutMapping("/{reviewId}")
    override fun updateReview(
        @Auth auth: AuthContext,
        @PathVariable reviewId: Long,
        @RequestBody request: UpdateReviewRequest,
    ) {
        val memberId = auth.memberId()
        updateReviewUseCase.update(request.toCommand(reviewId = reviewId, memberId = memberId))
    }

    @DeleteMapping("/{reviewId}")
    override fun deleteReview(
        @Auth auth: AuthContext,
        @PathVariable reviewId: Long,
    ) {
        val memberId = auth.memberId()
        deleteReviewUseCase.delete(memberId = memberId, reviewId = reviewId)
    }

    @PostMapping("/helpful/{reviewId}")
    override fun clickHelpfulReview(
        @Auth auth: AuthContext,
        @PathVariable reviewId: Long,
    ) {
        val memberId = auth.memberId()
        clickHelpfulReviewUseCase.clickHelpfulReview(memberId = memberId, reviewId = reviewId)
    }

    @DeleteMapping("/helpful/{reviewId}")
    override fun deleteHelpfulReview(
        @Auth auth: AuthContext,
        @PathVariable reviewId: Long,
    ) {
        val memberId = auth.memberId()
        deleteHelpfulReviewUseCase.deleteHelpfulReview(memberId = memberId, reviewId = reviewId)
    }

    @GetMapping("/restaurants/{restaurantId}")
    override fun readAllRestaurantsReviews(
        @Auth auth: AuthContext,
        @PathVariable restaurantId: Long,
        @RequestParam(name = "onlyPhotoReview", required = false, defaultValue = "false") onlyPhotoReview: Boolean,
        @RequestParam("sort", required = false, defaultValue = "highRating") sortCondition: String,
        @PageableDefault(size = 10, page = 0) pageable: Pageable,
    ): SliceResponse<ReviewPreviewResponse> {
        val sort = ReadReviewSortCondition.from(sortCondition)
        val reviews = readRestaurantReviewsUseCase.readAll(
            restaurantId = restaurantId,
            onlyPhotoReview = onlyPhotoReview,
            page = pageable.pageNumber,
            size = pageable.pageSize,
            memberId = auth.optionalMemberId(),
            sort = sort,
        )
        return SliceResponse.from(
            sliceResult = reviews,
            converter = ReviewPreviewResponse::from,
        )
    }

    @GetMapping("/restaurants/{restaurantId}/count")
    override fun readAmountOfRestaurantsReviews(
        @PathVariable restaurantId: Long,
    ): Int {
        return readAmountOfRestaurantReviewsUseCase.readAmountOfRestaurantReviews(restaurantId)
    }

    @GetMapping("/{reviewId}")
    override fun readReview(
        @Auth auth: AuthContext,
        @PathVariable reviewId: Long,
    ): SingleReviewResponse {
        return SingleReviewResponse.from(
            readSingleReviewUseCase.read(
                reviewId = reviewId,
                memberId = auth.optionalMemberId(),
            ),
        )
    }

    @GetMapping("/my")
    override fun readMyReviews(
        @Auth auth: AuthContext,
    ): List<ReviewPreviewResponse> {
        val reviews = readMyReviewsUseCase.readMyReviews(auth.memberId())
        return reviews.map(ReviewPreviewResponse::from)
    }
}
