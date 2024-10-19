package com.celuveat.review.adapter.`in`.rest.request

enum class ReadReviewSortCondition(
    private val value: String,
) {
    HIGH_RATING("high_rating"), // 별점 높은 순
    LOW_RATING("low_rating"), // 별점 낮은 순
    HELPFUL("helpful"), // 도움돼요 순
    CREATED_AT("createdAt"), // 최신 순
    ;

    companion object {
        fun from(sortCondition: String): ReadReviewSortCondition {
            return ReadReviewSortCondition.entries
                .findLast { it.value.equals(sortCondition, ignoreCase = true) }
                ?: throw IllegalArgumentException("정렬 조건은 high_rating, low_rating, review, createdAt 중 하나입니다. 현재 : $sortCondition")
        }
    }
}
