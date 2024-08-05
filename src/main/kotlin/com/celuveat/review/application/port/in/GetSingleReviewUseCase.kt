package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.result.SingleReviewResult

interface GetSingleReviewUseCase {

    fun get(id: Long): SingleReviewResult
}
