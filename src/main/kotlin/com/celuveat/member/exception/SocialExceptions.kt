package com.celuveat.member.exception

import com.celuveat.common.exception.CeluveatException
import com.celuveat.member.domain.SocialLoginType
import org.springframework.http.HttpStatus

sealed class SocialException(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data class NotSupportedSocialLoginTypeException(
    val socialLoginType: SocialLoginType,
) : SocialException(HttpStatus.NOT_FOUND, "${socialLoginType.name}은 지원하지 않습니다")

data object NotAllowedRedirectUriException : SocialException(HttpStatus.BAD_REQUEST, "허용되지 않은 redirect uri입니다") {
    private fun readResolve(): Any = NotAllowedRedirectUriException
}
