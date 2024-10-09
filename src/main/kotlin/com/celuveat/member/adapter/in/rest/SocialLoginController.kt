package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.member.adapter.`in`.rest.response.LoginResponse
import com.celuveat.member.application.port.`in`.ReadSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.`in`.WithdrawSocialLoginUseCase
import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand
import com.celuveat.member.domain.SocialLoginType
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
    private val readSocialLoginUrlUseCase: ReadSocialLoginUrlUseCase,
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
    ): String {
        val socialLoginUrl = readSocialLoginUrlUseCase.getSocialLoginUrl(socialLoginType, requestOrigin)
        return socialLoginUrl
    }

    @DeleteMapping("/withdraw")
    override fun withdraw(
        @Auth auth: AuthContext,
        @RequestHeader(HttpHeaders.ORIGIN) requestOrigin: String,
    ): ResponseEntity<Unit> {
        val memberId = auth.memberId()
        val command = WithdrawSocialLoginCommand(memberId, requestOrigin)
        withdrawSocialLoginUseCase.withdraw(command)
        return ResponseEntity.noContent().build()
    }
}
