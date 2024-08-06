package com.celuveat.review.domain

import com.celuveat.member.domain.Member

data class HelpfulReview(
    val id: Long = 0,
    val review: Review,
    val clicker: Member
) {
    fun unClick() {
        review.unClickHelpful()
    }
}