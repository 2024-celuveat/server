package com.celuveat.member.adapter.out.oauth.naver

import com.celuveat.member.adapter.out.oauth.naver.response.NaverMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.naver.response.NaverSocialLoginToken
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface NaverApiClient {
    // ref - https://developers.naver.com/docs/login/api/api.md
    @PostExchange(url = "https://nid.naver.com/oauth2.0/token")
    fun fetchToken(
        @RequestParam params: Map<String, String>,
    ): NaverSocialLoginToken

    // ref - https://developers.naver.com/docs/login/profile/profile.md
    @GetExchange("https://openapi.naver.com/v1/nid/me")
    fun fetchMemberInfo(
        @RequestHeader(name = AUTHORIZATION) bearerToken: String,
    ): NaverMemberInfoResponse
}
