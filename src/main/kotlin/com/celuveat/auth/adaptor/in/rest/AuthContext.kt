package com.celuveat.auth.adaptor.`in`.rest

import com.celuveat.auth.exception.UnAuthorizationException

data class AuthContext(
    private val memberId: Long?,
) {
    fun asGuest(): Long? {
        return this.memberId
    }

    fun memberId(): Long {
        return this.memberId ?: throw UnAuthorizationException
    }

    companion object {
        fun guest(): AuthContext {
            return AuthContext(null)
        }
    }
}
