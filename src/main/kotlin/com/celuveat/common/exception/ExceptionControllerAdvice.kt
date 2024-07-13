package com.celuveat.common.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {

    private val log = LoggerFactory.getLogger(ExceptionControllerAdvice::class.java)!!

    @ExceptionHandler(CeluveatException::class)
    fun handleBaseException(request: HttpServletRequest, e: CeluveatException): ResponseEntity<ExceptionResponse> {
        log.warn(
            """
            잘못된 요청이 들어왔습니다.
            ERROR TYPE: ${e.javaClass.simpleName}
            URI: ${request.requestURI}
            내용: ${e.errorMessage}
            """
        )
        return ResponseEntity.status(e.status).body(
            ExceptionResponse(e.errorMessage)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(request: HttpServletRequest, e: Exception): ResponseEntity<ExceptionResponse> {
        log.error("예상하지 못한 예외가 발생했습니다. URI: ${request.requestURI}, ${e.message}", e)
        return ResponseEntity.internalServerError().body(
            ExceptionResponse("서버가 응답할 수 없습니다.")
        )
    }
}
