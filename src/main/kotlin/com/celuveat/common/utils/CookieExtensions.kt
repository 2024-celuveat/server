package com.celuveat.common.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

inline fun HttpServletResponse.addSecureCookie(
    name: String,
    value: String,
    path: String = "/",
    maxAge: Int = -1,
    isHttpOnly: Boolean = true,
    isSecure: Boolean = true
) {
    val cookie = Cookie(name, value).apply {
        this.isHttpOnly = isHttpOnly
        this.secure = isSecure
        this.path = path
        this.maxAge = maxAge
    }
    this.addCookie(cookie)
}
