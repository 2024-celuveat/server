package com.celuveat.member.adapter.`in`.rest.response

import com.celuveat.auth.domain.Token
import io.swagger.v3.oas.annotations.media.Schema

data class LoginResponse(
    @Schema(
        description = "JWT 토큰",
        example = "accesstoken",
    )
    val accessToken: String,
) {
    companion object {
        fun from(token: Token): LoginResponse {
            return LoginResponse(accessToken = token.token)
        }
    }
}
