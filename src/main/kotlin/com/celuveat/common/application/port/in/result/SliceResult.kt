package com.celuveat.common.application.port.`in`.result

import org.springframework.data.domain.Slice

data class SliceResult<T>(
    val contents: List<T>,
    val currentPage: Int,
    val hasNext: Boolean,
    val size: Int,
) {
    fun <R> convertContent(converter: (T) -> R): SliceResult<R> {
        return SliceResult(
            contents = contents.map { converter(it) },
            currentPage = currentPage,
            hasNext = hasNext,
            size = size,
        )
    }

    companion object {
        fun <T> of(
            contents: List<T>,
            currentPage: Int,
            hasNext: Boolean,
        ): SliceResult<T> {
            return SliceResult(contents, currentPage, hasNext, contents.size)
        }

        fun <T> from(
            contents: Slice<T>
        ): SliceResult<T> {
            return SliceResult(contents.content, contents.number, contents.hasNext(), contents.size)
        }
    }
}
