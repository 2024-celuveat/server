package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.auth.domain.Token
import com.celuveat.member.application.port.`in`.GetSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.`in`.WithdrawSocialLoginUseCase
import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand
import com.celuveat.member.domain.SocialLoginType
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.unmockkAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get

@WebMvcTest(SocialLoginController::class)
class SocialLoginControllerTest(
    @Autowired val mockMvc: MockMvc,
    @MockkBean val socialLoginUseCase: SocialLoginUseCase,
    @MockkBean val createAccessTokenUseCase: CreateAccessTokenUseCase,
    @MockkBean val getSocialLoginUrlUseCase: GetSocialLoginUrlUseCase,
    @MockkBean val withdrawSocialLoginUseCase: WithdrawSocialLoginUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({

    context("OAuth 서비스로부터 받은 인가코드로 로그인을 요청한다") {
        val authCode = "authCode"
        val socialLoginType = SocialLoginType.KAKAO
        val requestOrigin = "http://localhost:3000"
        val jwtAccessToken = Token("accessToken")

        test("소셜 로그인 성공") {
            val command = SocialLoginCommand(socialLoginType, authCode, requestOrigin)
            every { socialLoginUseCase.login(command) } returns 1L
            every { createAccessTokenUseCase.create(1L) } returns jwtAccessToken

            mockMvc.get("/social-login/{socialLoginType}", socialLoginType) {
                param("authCode", authCode)
                header("Origin", requestOrigin)
            }.andExpect {
                status { isOk() }
                jsonPath("$.accessToken") { value("accessToken") }
            }.andDo {
                print()
            }
        }

        test("지원하지 않는 서버 타입으로 요청 하면 실패한다") {
            val unsupportedSocialLoginType = "UNSUPPORTED"

            mockMvc.get("/social-login/{socialLoginType}", unsupportedSocialLoginType) {
                param("authCode", authCode)
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.errorMessage") { value("잘못된 요청입니다.") }
            }
        }
    }

    context("소셜 로그인 URL을 요청한다") {
        val socialLoginType = SocialLoginType.KAKAO
        val requestOrigin = "http://localhost:3000"
        val socialLoginUrl = "https://social.com/authorize?redirect_uri=$requestOrigin&client_id=clientId"

        test("소셜 로그인 URL을 성공적으로 반환한다") {
            every { getSocialLoginUrlUseCase.getSocialLoginUrl(socialLoginType, requestOrigin) } returns socialLoginUrl

            mockMvc.get("/social-login/url/{socialLoginType}", socialLoginType) {
                header("Origin", requestOrigin)
            }.andExpect {
                status { isFound() }
                header { string("Location", socialLoginUrl) }
            }
        }

        test("지원하지 않는 서버 타입으로 요청 하면 실패한다") {
            val unsupportedSocialLoginType = "UNSUPPORTED"

            mockMvc.get("/social-login/url/{socialLoginType}", unsupportedSocialLoginType) {
                header("Origin", requestOrigin)
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.errorMessage") { value("잘못된 요청입니다.") }
            }
        }
    }

    context("회원 탈퇴를 요청한다") {
        val socialLoginType = SocialLoginType.KAKAO
        val authCode = "authCode"
        val accessToken = "celuveatAccessToken"
        val requestOrigin = "http://localhost:3000"
        val memberId = 1L

        test("소셜 로그인 회원 탈퇴 성공") {
            val command = WithdrawSocialLoginCommand(memberId, authCode, socialLoginType, requestOrigin)
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { withdrawSocialLoginUseCase.withdraw(command) } returns Unit

            mockMvc.delete("/social-login/withdraw/{socialLoginType}", socialLoginType) {
                param("authCode", authCode)
                header("Origin", requestOrigin)
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isNoContent() }
            }
        }
    }
}) {
    override suspend fun afterEach(
        testCase: TestCase,
        result: TestResult,
    ) {
        clearAllMocks()
        unmockkAll()
    }
}
