package com.celuveat.member.exception

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus

sealed class MemberException(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object NotFoundMemberException : MemberException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다") {
    private fun readResolve(): Any = NotFoundMemberException
}
