package com.celuveat.review.application.port.out

import com.celuveat.review.domain.Review

interface DeleteReviewPort {
    fun delete(review: Review)
}
