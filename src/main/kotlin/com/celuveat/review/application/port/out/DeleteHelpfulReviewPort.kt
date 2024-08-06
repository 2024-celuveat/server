package com.celuveat.review.application.port.out

import com.celuveat.review.domain.HelpfulReview

interface DeleteHelpfulReviewPort {

    fun deleteHelpfulReview(helpfulReview: HelpfulReview)
}
