package com.celuveat.common.utils

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie

inline fun HttpServletResponse.addSecureCookie(
    name: String,
    value: String,
    path: String = "/",
    maxAge: Long = -1,
    isHttpOnly: Boolean = true,
    isSecure: Boolean = true,
    sameSite: String = "Lax",
) {
    val cookie = ResponseCookie.from(name, value)
        .path(path)
        .sameSite(sameSite)
        .httpOnly(isHttpOnly)
        .secure(isSecure)
        .maxAge(maxAge)
        .build()

    this.addHeader("Set-Cookie", cookie.toString())
}

inline fun HttpServletResponse.expireCookie(
    name: String,
    path: String = "/",
    isHttpOnly: Boolean = true,
    isSecure: Boolean = true,
    sameSite: String = "Lax",
) {
    val expiredCookie = ResponseCookie.from(name, "")
        .path(path)
        .sameSite(sameSite)
        .httpOnly(isHttpOnly)
        .secure(isSecure)
        .maxAge(0) // 즉시 만료
        .build()

    this.addHeader("Set-Cookie", expiredCookie.toString())
}
