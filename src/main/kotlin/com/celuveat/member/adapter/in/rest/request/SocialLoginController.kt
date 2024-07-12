package com.celuveat.member.adapter.`in`.rest.request

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
) {

    @GetMapping("/login/{socialType}")
    fun login(
        @PathVariable socialType: String, // converter로 한번에..?
        @RequestParam authCode: String,
    ): Long {
        val socialLoginType = SocialLoginType.from(socialType)
        val memberId = socialLoginUseCase.login(socialLoginType, authCode)
        // authUseCase?
        return 1L
    }

}
