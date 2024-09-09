package com.celuveat.member.adapter.`in`.rest

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.member.adapter.`in`.rest.request.UpdateProfileRequest
import com.celuveat.member.adapter.`in`.rest.response.MemberProfileResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "회원 API")
interface MemberApi {
    @Operation(summary = "회원 정보 조회")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/profile")
    fun readMember(
        @Auth auth: AuthContext,
    ): MemberProfileResponse

    @Operation(summary = "회원 정보 조회")
    @SecurityRequirement(name = "JWT")
    @PatchMapping("/profile")
    fun updateMember(
        @Auth auth: AuthContext,
        @RequestBody request: UpdateProfileRequest,
    )
}
