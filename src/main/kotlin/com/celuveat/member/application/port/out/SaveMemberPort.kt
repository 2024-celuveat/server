package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member

interface SaveMemberPort {
    fun save(member: Member): Member
}
