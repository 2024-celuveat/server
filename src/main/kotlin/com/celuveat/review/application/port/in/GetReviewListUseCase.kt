package com.celuveat.review.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult

interface GetReviewListUseCase {
    fun getAll(
        restaurantId: Long,
        page: Int,
        size: Int,
    ): SliceResult<ReviewPreviewResult>
}
