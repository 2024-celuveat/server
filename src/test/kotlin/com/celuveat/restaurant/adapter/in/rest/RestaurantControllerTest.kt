package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.support.sut
import com.fasterxml.jackson.databind.ObjectMapper
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
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
import org.springframework.test.web.servlet.post

@WebMvcTest(RestaurantController::class)
class RestaurantControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
    @MockkBean val readInterestedRestaurantsUseCase: ReadInterestedRestaurantsUseCase,
    @MockkBean val addInterestedRestaurantsUseCase: AddInterestedRestaurantsUseCase,
    @MockkBean val deleteInterestedRestaurantsUseCase: DeleteInterestedRestaurantsUseCase,
    @MockkBean val readCelebrityVisitedRestaurantUseCase: ReadCelebrityVisitedRestaurantUseCase,
    @MockkBean val readCelebrityRecommendRestaurantsUseCase: ReadCelebrityRecommendRestaurantsUseCase,
    // for AuthMemberArgumentResolver
    @MockkBean val extractMemberIdUseCase: ExtractMemberIdUseCase,
) : FunSpec({

    context("관심 음식점 목록을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val page = 0
        test("조회 성공") {
            val results = SliceResult.of(
                contents = sut.giveMeBuilder<RestaurantPreviewResult>()
                    .sampleList(3),
                currentPage = page,
                hasNext = false,
            )
            val response = SliceResponse.from(results, RestaurantPreviewResponse::from)
            val query = GetInterestedRestaurantsQuery(memberId, page, size = 3)

            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readInterestedRestaurantsUseCase.getInterestedRestaurant(query) } returns results

            mockMvc.get("/restaurants/interested") {
                header("Authorization", "Bearer $accessToken")
                param("page", page.toString())
                param("size", "3")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("관심 음식점으로 등록 한다") {
        val memberId = 1L
        val restaurantId = 1L
        val accessToken = "celuveatAccessToken"

        test("요청 성공") {
            val command = AddInterestedRestaurantCommand(memberId, restaurantId)

            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { addInterestedRestaurantsUseCase.addInterestedRestaurant(command) } returns Unit

            mockMvc.post("/restaurants/interested/{restaurantId}", restaurantId) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
            }
        }
    }

    context("관심 음식점을 삭제 한다") {
        val memberId = 1L
        val restaurantId = 1L
        val accessToken = "celuveatAccessToken"

        test("요청 성공") {
            val command = DeleteInterestedRestaurantCommand(memberId, restaurantId)

            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { deleteInterestedRestaurantsUseCase.deleteInterestedRestaurant(command) } returns Unit

            mockMvc.delete("/restaurants/interested/{restaurantId}", restaurantId) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andDo {
                print()
            }
        }
    }

    context("셀럽이 방문한 음식점 목록을 조회 한다") {
        val celebrityId = 1L
        val page = 0

        test("회원 조회 성공") {
            val memberId = 1L
            val accessToken = "celuveatAccessToken"
            val results = SliceResult.of(
                contents = sut.giveMeBuilder<RestaurantPreviewResult>()
                    .sampleList(3),
                currentPage = page,
                hasNext = false,
            )
            val response = SliceResponse.from(results, RestaurantPreviewResponse::from)
            val query = ReadCelebrityVisitedRestaurantQuery(memberId, celebrityId, page, size = 3)

            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readCelebrityVisitedRestaurantUseCase.readCelebrityVisitedRestaurant(query) } returns results

            mockMvc.get("/restaurants/celebrity/$celebrityId") {
                header("Authorization", "Bearer $accessToken")
                param("page", page.toString())
                param("size", "3")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }

        test("비회원 조회 성공") {
            val results = SliceResult.of(
                contents = sut.giveMeBuilder<RestaurantPreviewResult>()
                    .setExp(RestaurantPreviewResult::liked, false)
                    .sampleList(3),
                currentPage = page,
                hasNext = false,
            )
            val response = SliceResponse.from(results, RestaurantPreviewResponse::from)
            val query = ReadCelebrityVisitedRestaurantQuery(null, celebrityId, page, size = 3)

            every { readCelebrityVisitedRestaurantUseCase.readCelebrityVisitedRestaurant(query) } returns results

            mockMvc.get("/restaurants/celebrity/$celebrityId") {
                param("page", page.toString())
                param("size", "3")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("셀럽 추천 음식점 목록을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        test("회원 조회 성공") {
            val results = sut.giveMeBuilder<RestaurantPreviewResult>().sampleList(3)
            val response = results.map(RestaurantPreviewResponse::from)
            
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readCelebrityRecommendRestaurantsUseCase.readCelebrityRecommendRestaurants(any()) } returns results

            mockMvc.get("/restaurants/celebrity/recommend") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }

        test("비회원 조회 성공") {
            val results = sut.giveMeBuilder<RestaurantPreviewResult>()
                .setExp(RestaurantPreviewResult::liked, false)
                .sampleList(3)
            val response = results.map(RestaurantPreviewResponse::from)
            every { readCelebrityRecommendRestaurantsUseCase.readCelebrityRecommendRestaurants(any()) } returns results

            mockMvc.get("/restaurants/celebrity/recommend") {
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
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
