package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.command.WriteReviewCommand

interface WriteReviewUseCase {
    fun write(command: WriteReviewCommand): Long
}
