package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialLoginService(
    private val fetchSocialMemberPort: FetchSocialMemberPort,
    private val saveMemberPort: SaveMemberPort,
    private val findMemberPort: FindMemberPort,
) : SocialLoginUseCase {

    @Transactional
    override fun login(socialLoginType: SocialLoginType, authCode: String): Long {
        val member = fetchSocialMemberPort.fetchMember(socialLoginType, authCode)
        val signInMember = findMemberPort.findBySocialIdentifier(member.socialIdentifier)
            ?: saveMemberPort.save(member)
        return signInMember.id
    }
}
