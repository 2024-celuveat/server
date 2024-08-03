package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import org.springframework.stereotype.Service

@Service
class CelebrityService(
    private val findCelebritiesPort: FindCelebritiesPort,
) : GetInterestedCelebritiesUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = findCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it) }
    }
}
