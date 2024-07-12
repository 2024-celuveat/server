package com.celuveat.member.adapter.`in`.rest.response

import com.celuveat.auth.domain.Token

data class LoginResponse(
    val accessToken: String
) {
    companion object {
        fun from(token: Token): LoginResponse {
            return LoginResponse(accessToken = token.token)
        }
    }
}
