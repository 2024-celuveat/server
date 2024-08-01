package com.celuveat.common.application.port.`in`.result

data class SliceResult<T>(
    val contents: List<T>,
    val currentPage: Int,
    val hasNext: Boolean,
    val size: Int,
) {

    companion object {
        fun <T> of(
            contents: List<T>,
            currentPage: Int,
            hasNext: Boolean,
        ): SliceResult<T> {
            return SliceResult(contents, currentPage, hasNext, contents.size)
        }
    }
}
