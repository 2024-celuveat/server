package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.member.adapter.`in`.rest.response.MemberResponse
import com.celuveat.member.application.port.`in`.ReadMemberUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/members")
@RestController
class MemberController(
    private val readMemberUseCase: ReadMemberUseCase,
) : MemberApi {

    @GetMapping("/profile")
    override fun readMember(@Auth auth: AuthContext): MemberResponse {
        val memberId = auth.memberId()
        val result = readMemberUseCase.readMember(memberId)
        return MemberResponse.from(result)
    }
}
