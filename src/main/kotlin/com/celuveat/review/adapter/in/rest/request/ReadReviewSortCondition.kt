package com.celuveat.review.adapter.`in`.rest.request

enum class ReadReviewSortCondition(
    private val value: String,
) {
    HIGH_RATING("highRating"), // 별점 높은 순
    LOW_RATING("lowRating"), // 별점 낮은 순
    HELPFUL("helpful"), // 도움돼요 순
    CREATED_AT("createdAt"), // 최신 순
    ;

    companion object {
        fun from(sortCondition: String): ReadReviewSortCondition {
            return ReadReviewSortCondition.entries
                .findLast { it.value.equals(sortCondition, ignoreCase = true) }
                ?: throw IllegalArgumentException("정렬 조건은 highRating, lowRating, review, createdAt 중 하나입니다. 현재 : $sortCondition")
        }
    }
}
