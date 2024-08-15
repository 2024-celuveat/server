package com.celuveat.auth.adapter.`in`.rest

import com.celuveat.auth.exception.UnAuthorizationException

data class AuthContext(
    private val memberId: Long?,
) {
    fun optionalMemberId(): Long? {
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
