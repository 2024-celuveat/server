package com.celuveat.common.adapter.out.rest.response

import com.celuveat.common.application.port.`in`.result.SliceResult
import io.swagger.v3.oas.annotations.media.Schema

data class SliceResponse<T>(
    @Schema(description = "페이징 컨텐츠")
    val contents: List<T>,
    @Schema(description = "현재 페이지")
    val currentPage: Int,
    @Schema(description = "다음 페이지 존재 여부")
    val hasNext: Boolean,
    @Schema(description = "컨텐츠 크기")
    val size: Int,
) {
    companion object {
        fun <T, R> from(
            sliceResult: SliceResult<R>,
            converter: (R) -> T,
        ): SliceResponse<T> {
            return SliceResponse(
                contents = sliceResult.contents.map { converter(it) },
                currentPage = sliceResult.currentPage,
                hasNext = sliceResult.hasNext,
                size = sliceResult.size,
            )
        }
    }
}
