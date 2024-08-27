package com.celuveat.member.application.port.`in`

import com.celuveat.member.application.port.`in`.result.MemberProfileResult

interface ReadMemberUseCase {
    fun readMember(memberId: Long): MemberProfileResult
}
