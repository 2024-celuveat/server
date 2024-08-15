package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier

interface ReadMemberPort {
    fun findBySocialIdentifier(socialIdentifier: SocialIdentifier): Member?

    fun readById(id: Long): Member
}
