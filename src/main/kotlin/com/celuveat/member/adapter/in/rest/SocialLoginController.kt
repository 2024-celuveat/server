package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.member.adapter.`in`.rest.response.LoginResponse
import com.celuveat.member.application.port.`in`.GetSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.`in`.WithdrawSocialLoginUseCase
import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand
import com.celuveat.member.domain.SocialLoginType
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/social-login")
@RestController
class SocialLoginController(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val createAccessTokenUseCase: CreateAccessTokenUseCase,
    private val getSocialLoginUrlUseCase: GetSocialLoginUrlUseCase,
    private val withdrawSocialLoginUseCase: WithdrawSocialLoginUseCase,
) : SocialLoginApi {
    @GetMapping("/{socialLoginType}")
    override fun login(
        @PathVariable socialLoginType: SocialLoginType,
        @RequestParam authCode: String,
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): LoginResponse {
        val command = SocialLoginCommand(socialLoginType, authCode, requestOrigin)
        val memberId = socialLoginUseCase.login(command)
        val token = createAccessTokenUseCase.create(memberId)
        return LoginResponse.from(token)
    }

    @GetMapping("/url/{socialLoginType}")
    override fun redirectLoginUrl(
        @PathVariable socialLoginType: SocialLoginType,
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
        response: HttpServletResponse,
    ) {
        val socialLoginUrl = getSocialLoginUrlUseCase.getSocialLoginUrl(socialLoginType, requestOrigin)
        response.sendRedirect(socialLoginUrl)
    }

    @DeleteMapping("/withdraw/{socialLoginType}")
    override fun withdraw(
        @AuthId memberId: Long,
        @RequestParam authCode: String,
        @PathVariable socialLoginType: SocialLoginType,
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): ResponseEntity<Unit> {
        val command = WithdrawSocialLoginCommand(memberId, authCode, socialLoginType, requestOrigin)
        withdrawSocialLoginUseCase.withdraw(command)
        return ResponseEntity.noContent().build()
    }
}
