package com.celuveat.review.application.port.out

import com.celuveat.review.domain.HelpfulReview

interface FindHelpfulReviewPort {

    fun getByReviewAndMember(reviewId: Long, memberId: Long): HelpfulReview
    fun existsByReviewAndMember(reviewId: Long, memberId: Long): Boolean
}
