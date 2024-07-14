package com.celuveat.member.adapter.out.oauth

import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

interface SocialLoginClient {
    fun isSupports(socialLoginType: SocialLoginType): Boolean
    fun fetchMember(authCode: String): Member
}
