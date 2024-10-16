package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.celeb.adapter.`in`.rest.response.BestCelebrityResponse
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityWithInterestedResponse
import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.ReadBestCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.ReadCelebritiesInRestaurantConditionUseCase
import com.celuveat.celeb.application.port.`in`.ReadCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.ReadInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.query.ReadCelebritiesInRestaurantConditionQuery
import com.celuveat.celeb.application.port.`in`.query.ReadCelebrityQuery
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadRestaurantsRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/celebrities")
@RestController
class CelebrityController(
    private val readInterestedCelebritiesUseCase: ReadInterestedCelebritiesUseCase,
    private val readBestCelebritiesUseCase: ReadBestCelebritiesUseCase,
    private val addInterestedCelebrityUseCase: AddInterestedCelebrityUseCase,
    private val deleteInterestedCelebrityUseCase: DeleteInterestedCelebrityUseCase,
    private val readCelebrityUseCase: ReadCelebrityUseCase,
    private val readCelebritiesInRestaurantConditionUseCase: ReadCelebritiesInRestaurantConditionUseCase,
) : CelebrityApi {
    @GetMapping("/interested")
    override fun readInterestedCelebrities(
        @Auth auth: AuthContext,
    ): List<CelebrityResponse> {
        val memberId = auth.memberId()
        val celebritiesResults = readInterestedCelebritiesUseCase.getInterestedCelebrities(memberId)
        return celebritiesResults.map { CelebrityResponse.from(it) }
    }

    @PostMapping("/interested/{celebrityId}")
    override fun addInterestedCelebrity(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
    ) {
        val memberId = auth.memberId()
        val command = AddInterestedCelebrityCommand(memberId, celebrityId)
        addInterestedCelebrityUseCase.addInterestedCelebrity(command)
    }

    @DeleteMapping("/interested/{celebrityId}")
    override fun deleteInterestedCelebrity(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
    ) {
        val memberId = auth.memberId()
        val command = DeleteInterestedCelebrityCommand(memberId, celebrityId)
        deleteInterestedCelebrityUseCase.deleteInterestedCelebrity(command)
    }

    @GetMapping("/best")
    override fun readBestCelebrities(
        @Auth auth: AuthContext,
    ): List<BestCelebrityResponse> {
        val optionalMemberId = auth.optionalMemberId()
        val celebritiesResults = readBestCelebritiesUseCase.readBestCelebrities(optionalMemberId)
        return celebritiesResults.map { BestCelebrityResponse.from(it) }
    }

    @GetMapping("/{celebrityId}")
    override fun readCelebrity(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
    ): CelebrityWithInterestedResponse {
        val memberId = auth.optionalMemberId()
        val query = ReadCelebrityQuery(memberId, celebrityId)
        val celebrityResult = readCelebrityUseCase.readCelebrity(query)
        return CelebrityWithInterestedResponse.from(celebrityResult)
    }

    @GetMapping("/in/restaurants/condition")
    override fun readCelebritiesInRestaurantsCondition(
        @ModelAttribute request: ReadRestaurantsRequest,
    ): List<SimpleCelebrityResponse> {
        val query = ReadCelebritiesInRestaurantConditionQuery(
            category = request.category,
            region = request.region,
            searchArea = SquarePolygon.ofNullable(
                lowLongitude = request.lowLongitude,
                highLongitude = request.highLongitude,
                lowLatitude = request.lowLatitude,
                highLatitude = request.highLatitude,
            ),
        )

        val results = readCelebritiesInRestaurantConditionUseCase.readCelebritiesInRestaurantCondition(query)
        return results.map { SimpleCelebrityResponse.from(it) }
    }
}
