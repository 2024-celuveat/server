package com.celuveat.restaurant.adapter.`in`.rest.request

enum class ReadCelebrityVisitedRestaurantSortCondition(
    private val value: String,
) {
    LIKE("like"), // 좋아요 많은 순
    REVIEW("review"), // 리뷰 많은 순
    CREATED_AT("createdAt"), // 최신 순
    ;

    companion object {
        fun from(sortCondition: String): ReadCelebrityVisitedRestaurantSortCondition {
            return ReadCelebrityVisitedRestaurantSortCondition.entries
                .findLast { it.value.equals(sortCondition, ignoreCase = true) }
                ?: throw IllegalArgumentException("정렬 조건은 like, review, createdAt 중 하나입니다. 현재 : $sortCondition")
        }
    }
}
