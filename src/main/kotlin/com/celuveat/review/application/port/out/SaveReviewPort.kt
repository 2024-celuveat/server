package com.celuveat.review.application.port.out

import com.celuveat.review.domain.Review

interface SaveReviewPort {
    
    fun save(review: Review): Review
}
