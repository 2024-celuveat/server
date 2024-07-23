package com.celuveat.auth.application.port.`in`

interface ExtractMemberIdUseCase {
    fun extract(token: String): Long
}
