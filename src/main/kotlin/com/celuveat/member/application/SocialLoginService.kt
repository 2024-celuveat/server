package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.GetSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.GetSocialLoginUrlPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialLoginService(
    private val fetchSocialMemberPort: FetchSocialMemberPort,
    private val getSocialLoginUrlPort: GetSocialLoginUrlPort,
    private val saveMemberPort: SaveMemberPort,
    private val findMemberPort: FindMemberPort,
) : SocialLoginUseCase, GetSocialLoginUrlUseCase {
    @Transactional
    override fun login(command: SocialLoginCommand): Long {
        val member = fetchSocialMemberPort.fetchMember(
            command.socialLoginType,
            command.authCode,
            command.requestOrigin,
        )
        val signInMember = findMemberPort.findBySocialIdentifier(member.socialIdentifier)
            ?: saveMemberPort.save(member)
        return signInMember.id
    }

    override fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        redirectUrl: String,
    ): String {
        return getSocialLoginUrlPort.getSocialLoginUrl(socialLoginType, redirectUrl)
    }
}
