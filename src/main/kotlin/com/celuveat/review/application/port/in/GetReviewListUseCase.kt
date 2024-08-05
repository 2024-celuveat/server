package com.celuveat.review.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.port.`in`.result.ReviewPreviewResult
import org.springframework.data.domain.Pageable

interface GetReviewListUseCase {

    fun getAll(restaurantsId: Long, page: Pageable): SliceResult<ReviewPreviewResult>
}
