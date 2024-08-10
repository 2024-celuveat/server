package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.ReadInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import org.springframework.stereotype.Service

@Service
class CelebrityQueryService(
    private val readInterestedCelebritiesPort: ReadInterestedCelebritiesPort,
) : ReadInterestedCelebritiesUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = readInterestedCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it.celebrity) }
    }
}
