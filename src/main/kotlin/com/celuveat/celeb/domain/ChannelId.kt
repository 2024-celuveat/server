package com.celuveat.celeb.domain

import com.celuveat.celeb.exceptions.InvalidChannelIdException
import com.celuveat.common.utils.throwWhen

data class ChannelId(
    val value: String
) {

    init {
        throwWhen(value.isBlank()) { InvalidChannelIdException }
        throwWhen(!value.startsWith("@")) { InvalidChannelIdException }
    }
}
