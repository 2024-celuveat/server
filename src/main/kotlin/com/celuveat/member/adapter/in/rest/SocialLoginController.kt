package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.member.adapter.`in`.rest.response.LoginResponse
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.domain.SocialLoginType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/social-login")
@RestController
class SocialLoginController(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val createAccessTokenUseCase: CreateAccessTokenUseCase,
) {

    @GetMapping("/login/{socialLoginType}")
    fun login(
        @PathVariable socialLoginType: SocialLoginType,
        @RequestParam authCode: String,
    ): LoginResponse {
        val memberId = socialLoginUseCase.login(socialLoginType, authCode)
        val token = createAccessTokenUseCase.create(memberId)
        return LoginResponse.from(token)
    }
}
