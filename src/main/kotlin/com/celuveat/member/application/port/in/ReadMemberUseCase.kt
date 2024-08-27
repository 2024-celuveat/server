package com.celuveat.member.application.port.`in`

import com.celuveat.member.application.port.`in`.result.MemberResult

interface ReadMemberUseCase {
    fun readMember(memberId: Long): MemberResult
}
