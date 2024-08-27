package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.celeb.adapter.`in`.rest.response.VideoWithCelebrityResponse
import com.celuveat.celeb.application.port.`in`.ReadVideosByRestaurantUseCase
import com.celuveat.celeb.application.port.`in`.result.VideoWithCelebrityResult
import com.celuveat.support.sut
import com.fasterxml.jackson.databind.ObjectMapper
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(VideoController::class)
class VideoControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
    @MockkBean val readVideosByRestaurantUseCase: ReadVideosByRestaurantUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({
    context("음식점이 나온 영상을 조회 한다") {
        val restaurantId = 1L
        val results = sut.giveMeBuilder<VideoWithCelebrityResult>().sampleList(2)
        val response = results.map { VideoWithCelebrityResponse.from(it) }
        test("조회 성공") {
            every { readVideosByRestaurantUseCase.readVideosByRestaurant(restaurantId) } returns results

            mockMvc.get("/videos/in/restaurants/$restaurantId").andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }
})
