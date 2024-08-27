package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.member.adapter.`in`.rest.request.UpdateProfileRequest
import com.celuveat.member.adapter.`in`.rest.response.MemberProfileResponse
import com.celuveat.member.application.port.`in`.ReadMemberUseCase
import com.celuveat.member.application.port.`in`.UpdateProfileUseCase
import com.celuveat.member.application.port.`in`.result.MemberProfileResult
import com.celuveat.support.sut
import com.fasterxml.jackson.databind.ObjectMapper
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setNotNullExp
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.unmockkAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@WebMvcTest(MemberController::class)
class MemberControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
    @MockkBean val readMemberUseCase: ReadMemberUseCase,
    @MockkBean val updateProfileUseCase: UpdateProfileUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({

    context("회원 정보를 조회한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val result = sut.giveMeOne(MemberProfileResult::class.java)
        val response = MemberProfileResponse.from(result)

        test("회원 정보 조회 성공") {
            every { readMemberUseCase.readMember(memberId) } returns result
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId

            mockMvc.get("/members/profile") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }

        test("비회원인 경우 실패") {
            mockMvc.get("/members/profile") {
            }.andExpect {
                status { isUnauthorized() }
            }.andDo {
                print()
            }
        }
    }

    context("회원 정보를 수정한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val request = sut.giveMeBuilder<UpdateProfileRequest>()
            .setNotNullExp(UpdateProfileRequest::nickname)
            .setNotNullExp(UpdateProfileRequest::profileImageUrl)
            .sample()
        val command = request.toCommand(memberId)

        test("회원 정보 수정 성공") {
            every { updateProfileUseCase.updateProfile(command) } just Runs
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId

            mockMvc.patch("/members/profile") {
                header("Authorization", "Bearer $accessToken")
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
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
