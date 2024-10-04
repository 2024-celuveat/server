package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult

interface ReadMyReviewsUseCase {
    fun readMyReviews(memberId: Long): List<ReviewPreviewResult>
}
