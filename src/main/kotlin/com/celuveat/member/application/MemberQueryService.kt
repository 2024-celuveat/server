package com.celuveat.member.application

import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import com.celuveat.member.application.port.`in`.ReadMemberUseCase
import com.celuveat.member.application.port.`in`.result.MemberProfileResult
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.review.application.port.out.ReadReviewPort
import org.springframework.stereotype.Service

@Service
class MemberQueryService(
    private val readMemberPort: ReadMemberPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
    private val readInterestedCelebritiesPort: ReadInterestedCelebritiesPort,
    private val readReviewPort: ReadReviewPort,
) : ReadMemberUseCase {
    override fun readMember(memberId: Long): MemberProfileResult {
        val member = readMemberPort.readById(memberId)
        val interestedRestaurantCount = readInterestedRestaurantPort.countByMemberId(memberId)
        val interestedCelebrityCount = readInterestedCelebritiesPort.countByMemberId(memberId)
        val reviewCount = readReviewPort.countByWriterId(memberId)
        return MemberProfileResult.of(
            member = member,
            interestedCount = interestedRestaurantCount + interestedCelebrityCount,
            reviewCount = reviewCount,
        )
    }
}
