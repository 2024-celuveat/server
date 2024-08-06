package com.celuveat.review.application.port.`in`

interface DeleteHelpfulReviewUseCase {
    fun deleteHelpfulReview(memberId: Long, reviewId: Long)
}
