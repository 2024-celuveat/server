package com.celuveat.review.domain

import com.celuveat.review.exception.InvalidStarScoreException

enum class Star(
    val score: Int,
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
                ?: throw InvalidStarScoreException
        }
    }
}
