package com.celuveat.auth.application.port.`in`

import com.celuveat.auth.domain.Token

interface CreateAccessTokenUseCase {

    fun create(memberId: Long): Token
}
