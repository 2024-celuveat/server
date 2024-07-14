package com.celuveat.member.application.port.out

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

interface FetchSocialMemberPort {

    fun fetchMember(socialLoginType: SocialLoginType, authCode: String): Member
}
