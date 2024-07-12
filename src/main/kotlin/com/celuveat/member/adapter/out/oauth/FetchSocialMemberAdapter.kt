package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType

@Adapter
class FetchSocialMemberAdapter(
    private val socialLoginClients: Set<SocialLoginClient>,
) : FetchSocialMemberPort {

    override fun fetchMember(socialLoginType: SocialLoginType, authCode: String): Member {
        val socialLoginClient = getSocialLoginClient(socialLoginType)
        val socialLoginToken = socialLoginClient.fetchAccessToken(authCode)
        val socialLoginInfo = socialLoginClient.fetchMemberInfo(socialLoginToken.accessToken)
        return socialLoginInfo.toMember()
    }

    private fun getSocialLoginClient(socialLoginType: SocialLoginType): SocialLoginClient {
        return socialLoginClients.firstOrNull { it.isSupports(socialLoginType) }
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입: $socialLoginType") // TODO 예외 분리
    }
}
