package com.celuveat.review.application.port

import com.celuveat.common.application.port.`in`.result.SliceResult
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
) : ReadRestaurantReviewsUseCase, ReadSingleReviewUseCase {
    override fun readAll(
        memberId: Long?,
        restaurantId: Long,
        page: Int,
        size: Int,
    ): SliceResult<ReviewPreviewResult> {
        val reviewResults = readReviewPort.readAllByRestaurantId(restaurantId, page, size)
        val reviewHelpfulReviewMapping = memberId?.let {
            readHelpfulReviewPort.readHelpfulReviewByMemberAndReviews(it, reviewResults.contents)
                .map { helpful -> helpful.review }.toSet()
        } ?: emptySet()
        return reviewResults.convertContent { ReviewPreviewResult.of(it, reviewHelpfulReviewMapping.contains(it)) }
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
}
