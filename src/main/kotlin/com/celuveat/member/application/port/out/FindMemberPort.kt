package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType

interface FindMemberPort {

    fun findMemberByOAuthIdAndServerType(oAuthId: String, serverType: OAuthServerType): Member?
}
