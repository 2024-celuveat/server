package com.celuveat.review.application.port

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.port.`in`.GetReviewListUseCase
import com.celuveat.review.application.port.`in`.GetSingleReviewUseCase
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import com.celuveat.review.application.port.`in`.result.SingleReviewResult
import com.celuveat.review.application.port.out.FindReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import org.springframework.stereotype.Service

@Service
class ReviewQueryService(
    private val findReviewPort: FindReviewPort,
    private val saveReviewPort: SaveReviewPort,
) : GetReviewListUseCase, GetSingleReviewUseCase {

    override fun getAll(restaurantsId: Long, page: Int, size: Int): SliceResult<ReviewPreviewResult> {
        return findReviewPort.findAllByRestaurantId(restaurantsId, page, size)
            .convertContent { ReviewPreviewResult.from(it) }
    }

    override fun get(id: Long): SingleReviewResult {
        val review = findReviewPort.getById(id)
        review.increaseView()
        saveReviewPort.save(review)
        return SingleReviewResult.from(review)
    }
}
