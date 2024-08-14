package com.celuveat.common.exception

import org.springframework.http.HttpStatus

abstract class CeluveatException(
    val status: HttpStatus,
    val errorMessage: String,
) : RuntimeException(errorMessage)
