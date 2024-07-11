package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.OAuthSignInUseCase
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.OAuthRequestPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.OAuthServerType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthService(
    private val oAuthRequestPort: OAuthRequestPort,
    private val saveMemberPort: SaveMemberPort,
    private val findMemberPort: FindMemberPort,
) : OAuthSignInUseCase {

    @Transactional
    override fun signIn(serverType: OAuthServerType, authCode: String): Long {
        val oAuthToken = oAuthRequestPort.fetchOAuthToken(serverType, authCode)
        val userInfo = oAuthRequestPort.fetchUserInfo(serverType, oAuthToken.accessToken)
        val member = userInfo.toMember()
        val signInMember = findMemberPort.findMemberByOAuthIdAndServerType(member.oAuthId, serverType)
            ?: saveMemberPort.save(member)
        return signInMember.id
    }
}
