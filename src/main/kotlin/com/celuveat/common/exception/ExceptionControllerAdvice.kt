package com.celuveat.common.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ExceptionControllerAdvice {
    private val log = LoggerFactory.getLogger(ExceptionControllerAdvice::class.java)!!

    @ExceptionHandler(CeluveatException::class)
    fun handleBaseException(
        request: HttpServletRequest,
        e: CeluveatException,
    ): ResponseEntity<ExceptionResponse> {
        log.warn(
            """
            잘못된 요청이 들어왔습니다.
            ERROR TYPE: ${e.javaClass.simpleName}
            URI: ${request.requestURI}
            내용: ${e.errorMessage}
            """,
        )
        return ResponseEntity.status(e.status).body(
            ExceptionResponse(e.errorMessage),
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleConversionFailedException(
        request: HttpServletRequest,
        e: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ExceptionResponse> {
        log.warn(
            """
            잘못된 요청이 들어왔습니다.
            URI: ${request.requestURI}
            내용: ${e.message}
            """,
        )
        return ResponseEntity.badRequest().body(
            ExceptionResponse("잘못된 요청입니다."),
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(
        request: HttpServletRequest,
        e: NoResourceFoundException,
    ): ResponseEntity<ExceptionResponse> {
        log.warn(
            """
            존재하지 않는 리소스에 요청이 들어왔습니다.
            URI: ${request.requestURI}
            내용: ${e.message}
            """,
        )
        return ResponseEntity.status(NOT_FOUND).body(
            ExceptionResponse("요청한 리소스를 찾을 수 없습니다."),
        )
    }

    @ExceptionHandler(MissingRequestValueException::class)
    fun handleMissingRequestValueException(
        request: HttpServletRequest,
        e: MissingRequestValueException,
    ): ResponseEntity<ExceptionResponse> {
        log.warn(
            """
            요청에 필요한 값이 누락되었습니다.
            URI: ${request.requestURI}
            내용: ${e.message}
            """,
        )
        return ResponseEntity.badRequest().body(
            ExceptionResponse("요청에 필요한 값이 누락되었습니다."),
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        request: HttpServletRequest,
        e: Exception,
    ): ResponseEntity<ExceptionResponse> {
        log.error("예상하지 못한 예외가 발생했습니다. URI: ${request.requestURI}, ${e.message}", e)
        return ResponseEntity.internalServerError().body(
            ExceptionResponse("서버가 응답할 수 없습니다."),
        )
    }
}
