package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType

interface FetchOAuthMemberPort {

    fun fetchMember(serverType: OAuthServerType, authCode: String): Member
}
