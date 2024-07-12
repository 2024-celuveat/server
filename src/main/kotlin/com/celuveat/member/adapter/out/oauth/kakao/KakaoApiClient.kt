package com.celuveat.member.adapter.out.oauth.kakao

import com.celuveat.member.adapter.out.oauth.kakao.response.KakaoMemberInfoResponse
import com.celuveat.member.application.port.out.response.SocialLoginToken
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface KakaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    fun fetchToken(@RequestParam body: Map<String, String>): SocialLoginToken

    @GetExchange(url = "https://kapi.kakao.com/v2/user/me")
    fun fetchMemberInfo(@RequestHeader(name = AUTHORIZATION) bearerToken: String): KakaoMemberInfoResponse
}
