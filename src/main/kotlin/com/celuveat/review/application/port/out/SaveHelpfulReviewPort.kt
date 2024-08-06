package com.celuveat.review.application.port.out

import com.celuveat.review.domain.HelpfulReview

interface SaveHelpfulReviewPort {

    fun save(helpfulReview: HelpfulReview): HelpfulReview
}
