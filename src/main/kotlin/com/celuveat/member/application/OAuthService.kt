package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.out.FetchOAuthMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.OAuthServerType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthService(
    private val fetchOAuthMemberPort: FetchOAuthMemberPort,
    private val saveMemberPort: SaveMemberPort,
    private val findMemberPort: FindMemberPort,
) : SocialLoginUseCase {

    @Transactional
    override fun login(serverType: OAuthServerType, authCode: String): Long {
        val member = fetchOAuthMemberPort.fetchMember(serverType, authCode)
        val signInMember = findMemberPort.findMemberByOAuthIdAndServerType(member.oAuthId, serverType)
            ?: saveMemberPort.save(member)
        return signInMember.id
    }
}
