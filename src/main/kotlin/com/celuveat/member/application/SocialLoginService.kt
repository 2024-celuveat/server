package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.ReadSocialLoginUrlUseCase
import com.celuveat.member.application.port.`in`.SocialLoginUseCase
import com.celuveat.member.application.port.`in`.WithdrawSocialLoginUseCase
import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand
import com.celuveat.member.application.port.out.DeleteMemberPort
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.member.application.port.out.ReadSocialLoginUrlPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.application.port.out.WithdrawSocialMemberPort
import com.celuveat.member.domain.SocialLoginType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialLoginService(
    private val fetchSocialMemberPort: FetchSocialMemberPort,
    private val readSocialLoginUrlPort: ReadSocialLoginUrlPort,
    private val saveMemberPort: SaveMemberPort,
    private val readMemberPort: ReadMemberPort,
    private val deleteMemberPort: DeleteMemberPort,
    private val withdrawSocialMemberPort: WithdrawSocialMemberPort,
) : SocialLoginUseCase, ReadSocialLoginUrlUseCase, WithdrawSocialLoginUseCase {
    @Transactional
    override fun login(command: SocialLoginCommand): Long {
        val member = fetchSocialMemberPort.fetchMember(
            command.socialLoginType,
            command.authCode,
            command.requestOrigin,
        )
        val signInMember = readMemberPort.findBySocialIdentifier(member.socialIdentifier)
            ?: saveMemberPort.save(member)
        return signInMember.id
    }

    override fun getSocialLoginUrl(
        socialLoginType: SocialLoginType,
        requestOrigin: String,
    ): String {
        return readSocialLoginUrlPort.readSocialLoginUrl(socialLoginType, requestOrigin)
    }

    @Transactional
    override fun withdraw(command: WithdrawSocialLoginCommand) {
        withdrawSocialMemberPort.withdraw(
            command.authCode,
            command.socialLoginType,
            command.requestOrigin,
        )
        deleteMemberPort.deleteById(command.memberId)
    }
}
