package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ToggleInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.ToggleInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
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
import org.springframework.test.web.servlet.post

@WebMvcTest(RestaurantController::class)
class RestaurantControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
    @MockkBean val getInterestedRestaurantsUseCase: GetInterestedRestaurantsUseCase,
    @MockkBean val toggleInterestedRestaurantUseCase: ToggleInterestedRestaurantsUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({

    context("관심 목록의 음식점을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val page = 0
        val results = SliceResult.of(
            contents = sut.giveMeBuilder<RestaurantPreviewResult>()
                .sampleList(3),
            currentPage = page,
            hasNext = false,
        )
        test("조회 성공") {
            val query = GetInterestedRestaurantsQuery(memberId, page, size = 3)
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { getInterestedRestaurantsUseCase.getInterestedRestaurant(query) } returns results

            mockMvc.get("/restaurants/interested") {
                header("Authorization", "Bearer $accessToken")
                param("page", page.toString())
                param("size", "3")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(results)) }
            }.andDo {
                print()
            }
        }
    }

    context("관심 음식점을 추가/삭제를 요청 한다") {
        val memberId = 1L
        val restaurantId = 1L
        val accessToken = "celuveatAccessToken"

        test("요청 성공") {
            val command = ToggleInterestedRestaurantCommand(memberId, restaurantId)
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { toggleInterestedRestaurantUseCase.toggleInterestedRestaurant(command) } returns Unit

            mockMvc.post("/restaurants/interested/$restaurantId") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
            }
        }
    }
})
