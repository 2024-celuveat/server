package com.celuveat.member.adapter.out.oauth.google

import com.celuveat.member.adapter.out.oauth.google.response.GoogleMemberInfoResponse
import com.celuveat.member.adapter.out.oauth.google.response.GoogleSocialLoginToken
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface GoogleApiClient {
    @PostExchange(url = "https://oauth2.googleapis.com/token")
    fun fetchToken(
        @RequestParam params: Map<String, String>,
    ): GoogleSocialLoginToken

    @GetExchange("https://www.googleapis.com/oauth2/v2/userinfo")
    fun fetchMemberInfo(
        @RequestHeader(AUTHORIZATION) bearerToken: String,
    ): GoogleMemberInfoResponse

    // ref - https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke
    @PostExchange("https://oauth2.googleapis.com/revoke")
    fun withdraw(
        @RequestParam token: String,
    )
}
