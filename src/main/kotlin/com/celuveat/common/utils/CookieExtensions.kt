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
    sameSite: String = "Lex",
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
