package com.celuveat.review.application

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.adapter.`in`.rest.request.ReadReviewSortCondition
import com.celuveat.review.application.port.`in`.ReadAmountOfRestaurantReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadMyReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadRestaurantReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadSingleReviewUseCase
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import com.celuveat.review.application.port.`in`.result.SingleReviewResult
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.ReadReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import org.springframework.stereotype.Service

@Service
class ReviewQueryService(
    private val readReviewPort: ReadReviewPort,
    private val readHelpfulReviewPort: ReadHelpfulReviewPort,
    private val saveReviewPort: SaveReviewPort,
) : ReadRestaurantReviewsUseCase, ReadSingleReviewUseCase, ReadAmountOfRestaurantReviewsUseCase, ReadMyReviewsUseCase {
    override fun readAll(
        memberId: Long?,
        onlyPhotoReview: Boolean,
        restaurantId: Long,
        sort: ReadReviewSortCondition,
        page: Int,
        size: Int,
    ): SliceResult<ReviewPreviewResult> {
        val reviewResults = readReviewPort.readAllByRestaurantId(restaurantId, onlyPhotoReview, sort, page, size)
        val reviewHelpfulReviewMapping = memberId?.let {
            readHelpfulReviewPort.readHelpfulReviewByMemberAndReviews(it, reviewResults.contents)
                .map { helpful -> helpful.review }.toSet()
        } ?: emptySet()
        return reviewResults.convertContent { ReviewPreviewResult.of(it, reviewHelpfulReviewMapping.contains(it)) }
    }

    override fun readAmountOfRestaurantReviews(restaurantId: Long): Int {
        return readReviewPort.countByRestaurantId(restaurantId)
    }

    override fun read(
        memberId: Long?,
        reviewId: Long,
    ): SingleReviewResult {
        val review = readReviewPort.readById(reviewId)
        review.increaseView()
        saveReviewPort.save(review)
        val clickedHelpful = memberId?.let {
            readHelpfulReviewPort.existsByReviewAndMember(reviewId = review.id, memberId = it)
        } ?: false
        return SingleReviewResult.of(review, clickedHelpful)
    }

    override fun readMyReviews(memberId: Long): List<ReviewPreviewResult> {
        val reviews = readReviewPort.readMyReviews(memberId)
        val reviewHelpfulReviewMapping = readHelpfulReviewPort.readHelpfulReviewByMemberAndReviews(memberId, reviews)
            .map { helpful -> helpful.review }.toSet()
        return reviews.map { ReviewPreviewResult.of(it, reviewHelpfulReviewMapping.contains(it)) }
    }
}
