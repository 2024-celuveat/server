package com.celuveat.auth.application.port.out

import com.celuveat.auth.domain.Token

interface CreateTokenPort {
    fun create(
        key: String,
        claim: String,
    ): Token

    fun create(claims: Map<String, String>): Token
}
