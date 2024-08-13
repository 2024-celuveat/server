package com.celuveat.review.application.port.out

import com.celuveat.review.domain.HelpfulReview
import com.celuveat.review.domain.Review

interface ReadHelpfulReviewPort {

    fun readHelpfulReviewByMemberAndReviews(
        memberId: Long,
        reviews: List<Review>,
    ): List<HelpfulReview>

    fun readByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): HelpfulReview

    fun existsByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): Boolean
}
