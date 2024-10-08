package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.member.adapter.`in`.rest.response.LoginResponse
import com.celuveat.member.domain.SocialLoginType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "소셜로그인 API")
interface SocialLoginApi {
    @Operation(summary = "소셜 로그인")
    @GetMapping("/{socialLoginType}")
    fun login(
        @Parameter(
            `in` = ParameterIn.PATH,
            required = true,
            description = "소셜 로그인 타입",
        )
        @PathVariable socialLoginType: SocialLoginType,
        @Parameter(
            `in` = ParameterIn.QUERY,
            required = true,
            description = "소셜 로그인 서비스에서 제공해주는 auth code",
        )
        @RequestParam authCode: String,
        @Parameter(
            `in` = ParameterIn.HEADER,
            required = true,
            description = "origin (실제 요청 시 별도로 설정하지 않아도 브라우저에서 넣어줌)",
            example = "http://localhost:3000",
        )
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): LoginResponse

    @Operation(summary = "소셜 로그인을 위한 Url 로 redirect")
    @GetMapping("/url/{socialLoginType}")
    fun redirectLoginUrl(
        @Parameter(
            `in` = ParameterIn.PATH,
            required = true,
            description = "소셜 로그인 타입",
        )
        @PathVariable socialLoginType: SocialLoginType,
        @Parameter(
            `in` = ParameterIn.HEADER,
            required = true,
            description = "origin (실제 요청 시 별도로 설정하지 않아도 브라우저에서 넣어줌)",
            example = "http://localhost:3000",
        )
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): String

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "소셜 회원 탈퇴")
    @DeleteMapping("/withdraw")
    fun withdraw(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.QUERY,
            required = true,
            description = "소셜 로그인 서비스에서 제공해주는 auth code",
        )
        @Parameter(
            `in` = ParameterIn.HEADER,
            required = true,
            description = "origin (실제 요청 시 별도로 설정하지 않아도 브라우저에서 넣어줌)",
            example = "http://localhost:3000",
        )
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): ResponseEntity<Unit>
}
