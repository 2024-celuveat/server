package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.member.adapter.`in`.rest.request.UpdateProfileRequest
import com.celuveat.member.adapter.`in`.rest.response.MemberProfileResponse
import com.celuveat.member.application.port.`in`.ReadMemberUseCase
import com.celuveat.member.application.port.`in`.UpdateProfileUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/members")
@RestController
class MemberController(
    private val readMemberUseCase: ReadMemberUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : MemberApi {

    @GetMapping("/profile")
    override fun readMember(@Auth auth: AuthContext): MemberProfileResponse {
        val memberId = auth.memberId()
        val result = readMemberUseCase.readMember(memberId)
        return MemberProfileResponse.from(result)
    }

    @PatchMapping("/profile")
    override fun updateMember(
        @Auth auth: AuthContext,
        request: UpdateProfileRequest,
    ) {
        val memberId = auth.memberId()
        val command = request.toCommand(memberId)
        updateProfileUseCase.updateProfile(command)
    }
}
