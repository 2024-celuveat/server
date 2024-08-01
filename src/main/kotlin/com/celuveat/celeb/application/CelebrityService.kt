package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.FindInterestedCelebritiesPort
import org.springframework.stereotype.Service

@Service
class CelebrityService(
    private val findInterestedCelebritiesPort: FindInterestedCelebritiesPort,
) : GetInterestedCelebritiesUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = findInterestedCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it) }
    }
}
