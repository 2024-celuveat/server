package com.celuveat.auth.application

import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.auth.application.port.out.CreateTokenPort
import com.celuveat.auth.application.port.out.ExtractClaimPort
import com.celuveat.auth.domain.Token
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val createTokenPort: CreateTokenPort,
    private val extractClaimPort: ExtractClaimPort
) : CreateAccessTokenUseCase, ExtractMemberIdUseCase {

    companion object {
        const val MEMBER_ID_CLAIM: String = "memberId"
    }

    override fun create(memberId: Long): Token {
        return createTokenPort.create(MEMBER_ID_CLAIM, memberId.toString())
    }

    override fun extract(token: String): Long {
        return extractClaimPort.extract(token, MEMBER_ID_CLAIM).toLong()
    }
}
