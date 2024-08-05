package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.support.sut
import com.fasterxml.jackson.databind.ObjectMapper
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(CelebrityController::class)
class CelebrityControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
    @MockkBean val getInterestedCelebritiesUseCase: GetInterestedCelebritiesUseCase,
    @MockkBean val addInterestedCelebrityUseCase: AddInterestedCelebrityUseCase,
    @MockkBean val deleteInterestedCelebrityUseCase: DeleteInterestedCelebrityUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({

    context("관심 셀럽의 목록을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val results = sut.giveMeBuilder<CelebrityResult>()
            .sampleList(3)
        test("조회 성공") {
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { getInterestedCelebritiesUseCase.getInterestedCelebrities(memberId) } returns results

            mockMvc.get("/celebrities/interested") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(results)) }
            }.andDo {
                print()
            }
        }
    }

    context("관심 셀럽을 추가 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val celebrityId = 1L
        val command = AddInterestedCelebrityCommand(memberId, celebrityId)

        test("추가 성공") {
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { addInterestedCelebrityUseCase.addInterestedCelebrity(command) } returns Unit

            mockMvc.post("/celebrities/interested/{celebrityId}", celebrityId) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
            }
        }
    }

    context("관심 셀럽을 삭제 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val celebrityId = 1L
        val command = DeleteInterestedCelebrityCommand(memberId, celebrityId)

        test("삭제 성공") {
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { deleteInterestedCelebrityUseCase.deleteInterestedCelebrity(command) } returns Unit

            mockMvc.delete("/celebrities/interested/{celebrityId}", celebrityId) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
            }
        }
    }
})
