package com.celuveat.review.application.port.`in`.command

import com.celuveat.review.domain.Star

data class UpdateReviewCommand(
    val memberId: Long,
    val reviewId: Long,
    var content: String,
    var star: Star,
    var images: List<String>,
)
