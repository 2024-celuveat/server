package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.UpdateProfileUseCase
import com.celuveat.member.application.port.`in`.command.UpdateProfileCommand
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val readMemberPort: ReadMemberPort,
    private val saveMemberPort: SaveMemberPort,
) : UpdateProfileUseCase {
    override fun updateProfile(command: UpdateProfileCommand) {
        val member = readMemberPort.readById(command.memberId)
        member.updateProfile(
            nickname = command.nickname,
            profileImageUrl = command.profileImageUrl,
        )
        saveMemberPort.save(member)
    }
}
