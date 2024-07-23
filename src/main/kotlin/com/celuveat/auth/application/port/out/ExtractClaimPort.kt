package com.celuveat.auth.application.port.out

interface ExtractClaimPort {
    fun extract(
        token: String,
        key: String,
    ): String

    fun extract(token: String): Map<String, String>
}
