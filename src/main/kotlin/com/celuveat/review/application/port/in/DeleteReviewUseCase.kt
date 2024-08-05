package com.celuveat.review.application.port.`in`

interface DeleteReviewUseCase {

    fun delete(memberId: Long, reviewId: Long)
}
