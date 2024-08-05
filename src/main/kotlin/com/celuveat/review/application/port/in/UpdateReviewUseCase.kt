package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.command.UpdateReviewCommand

interface UpdateReviewUseCase {

    fun update(command: UpdateReviewCommand)
}
