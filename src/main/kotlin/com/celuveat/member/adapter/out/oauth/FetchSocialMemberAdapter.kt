package com.celuveat.member.adapter.out.oauth

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.GetSocialLoginUrlPort
import com.celuveat.member.application.port.out.WithdrawSocialMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.member.exception.NotSupportedSocialLoginTypeException

@Adapter
class FetchSocialMemberAdapter(
    private val socialLoginClients: Set<SocialLoginClient>,
) : FetchSocialMemberPort, GetSocialLoginUrlPort, WithdrawSocialMemberPort {
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
        requestOrigin: String,
    ): String {
        val socialLoginClient = getSocialLoginClient(socialLoginType)
        return socialLoginClient.getSocialLoginUrl(requestOrigin)
    }

    override fun withdraw(
        authCode: String,
        socialLoginType: SocialLoginType,
        redirectUrl: String,
    ) {
        val socialLoginClient = getSocialLoginClient(socialLoginType)
        socialLoginClient.withdraw(authCode, redirectUrl)
    }
}
