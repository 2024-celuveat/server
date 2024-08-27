package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.ReadMemberUseCase
import com.celuveat.member.application.port.`in`.result.MemberResult
import com.celuveat.member.application.port.out.ReadMemberPort
import org.springframework.stereotype.Service

@Service
class MemberQueryService(
    private val readMemberPort: ReadMemberPort,
) : ReadMemberUseCase {
    override fun readMember(memberId: Long): MemberResult {
        val member = readMemberPort.readById(memberId)
        return MemberResult.from(member)
    }
}
