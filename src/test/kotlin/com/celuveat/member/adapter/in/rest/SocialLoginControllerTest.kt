package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.CreateAccessTokenUseCase
import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.auth.domain.Token
import com.celuveat.member.application.port.`in`.GetSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
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
import org.springframework.test.web.servlet.get

@WebMvcTest(SocialLoginController::class)
class SocialLoginControllerTest(
    @Autowired val mockMvc: MockMvc,
    @MockkBean val socialLoginUseCase: SocialLoginUseCase,
    @MockkBean val createAccessTokenUseCase: CreateAccessTokenUseCase,
    @MockkBean val getSocialLoginUrlUseCase: GetSocialLoginUrlUseCase,
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase, // for AuthMemberArgumentResolver
) : FunSpec({

    context("OAuth 서비스로부터 받은 인가코드로 로그인을 요청한다") {
        val authCode = "authCode"
        val socialLoginType = SocialLoginType.KAKAO
        val jwtAccessToken = Token("accessToken")

        test("소셜 로그인 성공") {
            every { socialLoginUseCase.login(socialLoginType, authCode) } returns 1L
            every { createAccessTokenUseCase.create(1L) } returns jwtAccessToken

            mockMvc.get("/social-login/login/{socialLoginType}", socialLoginType) {
                param("authCode", authCode)
            }.andExpect {
                status { isOk() }
                jsonPath("$.accessToken") { value("accessToken") }
            }.andDo {
                print()
            }
        }

        test("지원하지 않는 서버 타입으로 요청 하면 실패한다") {
            val unsupportedSocialLoginType = "UNSUPPORTED"

            mockMvc.get("/social-login/login/{socialLoginType}", unsupportedSocialLoginType) {
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
            every { getSocialLoginUrlUseCase.getSocialLoginUrl(requestOrigin, socialLoginType) } returns socialLoginUrl

            mockMvc.get("/social-login/login/{socialLoginType}/url", socialLoginType) {
                header("Origin", requestOrigin)
            }.andExpect {
                status { isFound() }
                header { string("Location", socialLoginUrl) }
            }
        }

        test("지원하지 않는 서버 타입으로 요청 하면 실패한다") {
            val unsupportedSocialLoginType = "UNSUPPORTED"

            mockMvc.get("/social-login/login/{socialLoginType}/url", unsupportedSocialLoginType) {
                header("Origin", requestOrigin)
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.errorMessage") { value("잘못된 요청입니다.") }
            }
        }
    }
}) {

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
        unmockkAll()
    }
}
