package com.celuveat.review.application.port

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.port.`in`.GetReviewListUseCase
import com.celuveat.review.application.port.`in`.GetSingleReviewUseCase
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import com.celuveat.review.application.port.`in`.result.SingleReviewResult
import com.celuveat.review.application.port.out.FindReviewPort
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ReviewQueryService(
    private val findReviewPort: FindReviewPort,
) : GetReviewListUseCase, GetSingleReviewUseCase {

    override fun getAll(restaurantsId: Long, page: Pageable): SliceResult<ReviewPreviewResult> {
        return findReviewPort.findAllByRestaurantId(restaurantsId, page)
            .convertContent { ReviewPreviewResult.from(it) }
    }

    override fun get(id: Long): SingleReviewResult {
        val review = findReviewPort.getById(id)
        return SingleReviewResult.from(review)
    }
}
