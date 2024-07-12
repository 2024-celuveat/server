package com.celuveat.member.domain

enum class SocialLoginType {
    KAKAO,
    ;

    companion object {
        fun from(serverTypeValue: String): SocialLoginType {
            return entries.find { it.name == serverTypeValue }
                ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입: $serverTypeValue") // TODO 예외 분리
        }
    }
}
