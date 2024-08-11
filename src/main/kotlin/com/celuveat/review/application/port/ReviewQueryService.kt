package com.celuveat.review.application.port

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.port.`in`.ReadReviewsUseCase
import com.celuveat.review.application.port.`in`.ReadSingleReviewUseCase
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import com.celuveat.review.application.port.`in`.result.SingleReviewResult
import com.celuveat.review.application.port.out.FindReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import org.springframework.stereotype.Service

// TODO test
@Service
class ReviewQueryService(
    private val findReviewPort: FindReviewPort,
    private val saveReviewPort: SaveReviewPort,
) : ReadReviewsUseCase, ReadSingleReviewUseCase {
    override fun readAll(
        restaurantId: Long,
        page: Int,
        size: Int,
    ): SliceResult<ReviewPreviewResult> {
        return findReviewPort.findAllByRestaurantId(restaurantId, page, size)
            .convertContent { ReviewPreviewResult.from(it) }
    }

    override fun read(id: Long): SingleReviewResult {
        val review = findReviewPort.getById(id)
        review.increaseView()
        saveReviewPort.save(review)
        return SingleReviewResult.from(review)
    }
}
