package com.celuveat.common.adapter.out.rest

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.NativeWebRequest

const val TOKEN_AUTHORIZATION_SCHEME = "Bearer "

inline fun NativeWebRequest.toHttpServletRequest(
    exceptionSupplier: () -> Exception = { IllegalStateException("Cannot access to HTTP request") },
): HttpServletRequest {
    return this.getNativeRequest(HttpServletRequest::class.java) ?: throw exceptionSupplier()
}

inline fun HttpServletRequest.getTokenAuthorizationOrThrow(
    exceptionSupplier: () -> Exception = { IllegalArgumentException("Authorization header not found") },
): String {
    val valueWithScheme = this.getHeader("Authorization") ?: throw exceptionSupplier()
    return valueWithScheme.removePrefix(TOKEN_AUTHORIZATION_SCHEME)
}
