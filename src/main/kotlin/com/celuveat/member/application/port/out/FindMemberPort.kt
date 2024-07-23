package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier

interface FindMemberPort {
    fun findBySocialIdentifier(socialIdentifier: SocialIdentifier): Member?
}
