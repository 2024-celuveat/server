package com.celuveat.review.domain

enum class Star(
    val score: Int
) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    ;

    companion object {
        fun from(score: Int): Star {
            return entries.find { it.score == score }
                ?: throw IllegalArgumentException("star value is must be 1 ~ 5, but current is $score")  // TODO throw internal server error
        }
    }
}
