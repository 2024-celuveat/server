package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.GetSocialLoginUrlPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.member.exception.NotSupportedSocialLoginTypeException

@Adapter
class FetchSocialMemberAdapter(
    private val socialLoginClients: Set<SocialLoginClient>,
) : FetchSocialMemberPort, GetSocialLoginUrlPort {
    override fun fetchMember(
        socialLoginType: SocialLoginType,
        authCode: String,
        redirectUrl: String,
    ): Member {
        val socialLoginClient = getSocialLoginClient(socialLoginType)
        return socialLoginClient.fetchMember(authCode, redirectUrl)
    }

    private fun getSocialLoginClient(socialLoginType: SocialLoginType): SocialLoginClient {
        return socialLoginClients.firstOrNull { it.isSupports(socialLoginType) }
            ?: throw NotSupportedSocialLoginTypeException(socialLoginType)
    }

    override fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        redirectUrl: String,
    ): String {
        val socialLoginClient = getSocialLoginClient(socialLoginType)
        return socialLoginClient.getSocialLoginUrl(redirectUrl)
    }
}
