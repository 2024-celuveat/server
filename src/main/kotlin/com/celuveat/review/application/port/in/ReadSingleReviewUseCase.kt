package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.result.SingleReviewResult

interface ReadSingleReviewUseCase {
    fun read(
        memberId: Long?,
        reviewId: Long,
    ): SingleReviewResult
}
