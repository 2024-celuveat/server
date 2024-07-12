package com.celuveat.auth.adaptor.out

import com.celuveat.auth.application.port.out.CreateTokenPort
import com.celuveat.auth.application.port.out.ExtractClaimPort
import com.celuveat.auth.domain.Token
import com.celuveat.common.annotation.Adapter
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

@Adapter
class TokenAdaptor(
    tokenProperty: TokenProperty,
) : CreateTokenPort, ExtractClaimPort {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(tokenProperty.secretKey))
    private val accessTokenExpirationMillis: Long = tokenProperty.accessTokenExpirationMillis

    override fun create(key: String, claim: String): Token {
        return create(mapOf(key to claim))
    }

    override fun create(claims: Map<String, String>): Token {
        return Token(
            Jwts.builder()
                .claims(claims)
                .issuedAt(Date())
                .expiration(Date(System.currentTimeMillis() + accessTokenExpirationMillis))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact()
        )
    }

    override fun extract(token: String, key: String): String {
        return extract(token)[key]
            ?: throw NoSuchElementException("Claim '$key' not found in token")  // TODO 예외처리
    }

    override fun extract(token: String): Map<String, String> {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .entries
                .associate { it.key to it.value.toString() }
        } catch (e: Exception) {
            throw RuntimeException("invalid JWT", e)  // TODO 예외처리
        }
    }
}