package com.celuveat.review.application.port.`in`.command

import com.celuveat.member.domain.Member
import com.celuveat.review.domain.Star

data class UpdateReviewCommand(
    val memberId: Long,
    val reviewId: Long,
    val writer: Member,
    var content: String,
    var star: Star,
)
