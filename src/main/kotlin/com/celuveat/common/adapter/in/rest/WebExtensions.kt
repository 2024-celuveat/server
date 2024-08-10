package com.celuveat.common.adapter.`in`.rest

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.NativeWebRequest

const val TOKEN_AUTHORIZATION_SCHEME = "Bearer "

inline fun NativeWebRequest.toHttpServletRequest(
    exceptionSupplier: () -> Exception = { IllegalStateException("Cannot access to HTTP request") },
): HttpServletRequest {
    return this.getNativeRequest(HttpServletRequest::class.java) ?: throw exceptionSupplier()
}

fun HttpServletRequest.getTokenAuthorizationOrNull(): String? {
    return this.getHeader("Authorization")?.removePrefix(TOKEN_AUTHORIZATION_SCHEME) ?: return null
}
