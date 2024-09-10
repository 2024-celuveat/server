package com.celuveat.restaurant.adapter.`in`.rest

import com.celuveat.auth.application.port.`in`.ExtractMemberIdUseCase
import com.celuveat.common.adapter.`in`.rest.response.SliceResponse
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantDetailResponse
import com.celuveat.restaurant.adapter.`in`.rest.response.RestaurantPreviewResponse
import com.celuveat.restaurant.application.port.`in`.AddInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.DeleteInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadNearbyRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadPopularRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantDetailUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadWeeklyUpdateRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantDetailResult
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
    @MockkBean val readRestaurantsUseCase: ReadRestaurantsUseCase,
    @MockkBean val readWeeklyUpdateRestaurantsUseCase: ReadWeeklyUpdateRestaurantsUseCase,
    @MockkBean val readNearbyRestaurantsUseCase: ReadNearbyRestaurantsUseCase,
    @MockkBean val readRestaurantDetailUseCase: ReadRestaurantDetailUseCase,
    @MockkBean val readPopularRestaurantsUseCase: ReadPopularRestaurantsUseCase,
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
            val query = ReadInterestedRestaurantsQuery(memberId, page, size = 3)

            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readInterestedRestaurantsUseCase.readInterestedRestaurant(query) } returns results

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

    context("조건에 따라 음식점 목록을 조회 한다") {
        val page = 0
        val size = 3

        test("회원 조회 성공") {
            val memberId = 1L
            val accessToken = "celuveatAccessToken"
            val category = "한식"
            val region = "성수"
            val results = sut.giveMeBuilder<RestaurantPreviewResult>()
                .setExp(RestaurantPreviewResult::liked, true)
                .sampleList(3)
            val sliceResult = SliceResult.of(
                contents = results,
                currentPage = page,
                hasNext = false,
            )
            val query = ReadRestaurantsQuery(
                memberId = memberId,
                region = region,
                category = category,
                searchArea = null,
                page = page,
                size = size,
            )
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readRestaurantsUseCase.readRestaurants(query) } returns sliceResult

            val response = SliceResponse.from(sliceResult, RestaurantPreviewResponse::from)
            mockMvc.get("/restaurants") {
                header("Authorization", "Bearer $accessToken")
                param("category", category)
                param("region", region)
                param("page", page.toString())
                param("size", size.toString())
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }

        test("지역 포함 조회 성공") {
            val memberId = 1L
            val accessToken = "celuveatAccessToken"
            val category = "한식"
            val region = "성수"
            val results = sut.giveMeBuilder<RestaurantPreviewResult>()
                .setExp(RestaurantPreviewResult::liked, true)
                .sampleList(3)
            val sliceResult = SliceResult.of(
                contents = results,
                currentPage = page,
                hasNext = false,
            )
            val query = ReadRestaurantsQuery(
                memberId = memberId,
                region = region,
                category = category,
                searchArea = SquarePolygon.ofNullable(
                    lowLongitude = 127.0,
                    highLongitude = 128.0,
                    lowLatitude = 35.0,
                    highLatitude = 36.0,
                ),
                page = page,
                size = size,
            )
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readRestaurantsUseCase.readRestaurants(query) } returns sliceResult

            val response = SliceResponse.from(sliceResult, RestaurantPreviewResponse::from)
            mockMvc.get("/restaurants") {
                header("Authorization", "Bearer $accessToken")
                param("category", category)
                param("region", region)
                param("lowLongitude", "127.0")
                param("highLongitude", "128.0")
                param("lowLatitude", "35.0")
                param("highLatitude", "36.0")
                param("page", page.toString())
                param("size", size.toString())
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("이번주에 업로드된 음식점 목록을 조회 한다") {
        val page = 0
        val size = 3

        test("회원 조회 성공") {
            val memberId = 1L
            val accessToken = "celuveatAccessToken"
            val results = sut.giveMeBuilder<RestaurantPreviewResult>()
                .setExp(RestaurantPreviewResult::liked, true)
                .sampleList(3)
            val sliceResult = SliceResult.of(
                contents = results,
                currentPage = page,
                hasNext = false,
            )
            val query = ReadWeeklyUpdateRestaurantsQuery(
                memberId = memberId,
                page = page,
                size = size,
            )
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readWeeklyUpdateRestaurantsUseCase.readWeeklyUpdateRestaurants(query) } returns sliceResult

            val response = SliceResponse.from(sliceResult, RestaurantPreviewResponse::from)
            mockMvc.get("/restaurants/weekly") {
                header("Authorization", "Bearer $accessToken")
                param("page", page.toString())
                param("size", size.toString())
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("주변 음식점을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        test("조회 성공") {
            val results = sut.giveMeBuilder<RestaurantPreviewResult>().sampleList(3)
            val response = results.map(RestaurantPreviewResponse::from)
            val query = ReadNearbyRestaurantsQuery(
                memberId = 1L,
                restaurantId = 1L,
            )
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readNearbyRestaurantsUseCase.readNearbyRestaurants(query) } returns results

            mockMvc.get("/restaurants/nearby/{restaurantId}", 1L) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("음식점을 조회 한다") {
        val memberId = 1L
        val accessToken = "celuveatAccessToken"
        val restaurantId = 1L
        test("조회 성공") {
            val results = sut.giveMeBuilder<RestaurantDetailResult>().sample()
            val response = RestaurantDetailResponse.from(results)
            val query = ReadRestaurantQuery(
                memberId = memberId,
                restaurantId = restaurantId,
            )
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId
            every { readRestaurantDetailUseCase.readRestaurantDetail(query) } returns results

            mockMvc.get("/restaurants/{restaurantId}", restaurantId) {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }
    }

    context("인기 음식점을 조회 한다") {
        val memberId = 1L
        test("회원 조회 성공") {
            val accessToken = "celuveatAccessToken"
            val results = sut.giveMeBuilder<RestaurantPreviewResult>().sampleList(3)
            val response = results.map(RestaurantPreviewResponse::from)

            every { readPopularRestaurantsUseCase.readPopularRestaurants(memberId) } returns results
            every { extractMemberIdUseCase.extract(accessToken) } returns memberId

            mockMvc.get("/restaurants/popular") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(response)) }
            }.andDo {
                print()
            }
        }

        test("비호원 조회 성공") {
            val results = sut.giveMeBuilder<RestaurantPreviewResult>()
                .setExp(RestaurantPreviewResult::liked, false)
                .sampleList(3)
            val response = results.map(RestaurantPreviewResponse::from)
            every { readPopularRestaurantsUseCase.readPopularRestaurants(null) } returns results

            mockMvc.get("/restaurants/popular") {
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
