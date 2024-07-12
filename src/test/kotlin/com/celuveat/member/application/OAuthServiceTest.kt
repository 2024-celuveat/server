package com.celuveat.member.application

import com.celuveat.common.application.ServiceTest
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify

class OAuthServiceTest : ServiceTest() {

    private val oAuthService = OAuthService(
        fetchOAuthMemberPort = mocks.fetchOAuthMemberPort,
        saveMemberPort = mocks.saveMemberPort,
        findMemberPort = mocks.findMemberPort,
    )

    init {
        Given("소셜 로그인을 통해 회원가입할 때") {
            val serverType = OAuthServerType.KAKAO
            val authCode = "authCode"
            val oAuthId = "oAuthId"

            val member = fixtureMonkey.giveMeBuilder<Member>()
                .set(Member::id, 0L)
                .set(Member::oAuthId, oAuthId)
                .set(Member::serverType, serverType)
                .sample()
            val savedMember = member.copy(id = 1L)

            When("최초 회원인 경우") {
                every { mocks.fetchOAuthMemberPort.fetchMember(serverType, authCode) } returns member
                every { mocks.findMemberPort.findMemberByOAuthIdAndServerType(member.oAuthId, serverType) } returns null
                every { mocks.saveMemberPort.save(member) } returns savedMember

                val result = oAuthService.login(serverType, authCode)

                Then("회원가입이 완료된다") {
                    result shouldBe 1L

                    verify { mocks.fetchOAuthMemberPort.fetchMember(serverType, authCode) }
                    verify { mocks.findMemberPort.findMemberByOAuthIdAndServerType(member.oAuthId, serverType) }
                    verify { mocks.saveMemberPort.save(member) }
                }
            }

            When("이미 가입된 회원인 경우") {
                every { mocks.fetchOAuthMemberPort.fetchMember(serverType, authCode) } returns member
                every {
                    mocks.findMemberPort.findMemberByOAuthIdAndServerType(
                        member.oAuthId,
                        serverType
                    )
                } returns savedMember

                val result = oAuthService.login(serverType, authCode)

                Then("로그인이 완료된다") {
                    result shouldBe savedMember.id

                    verify { mocks.fetchOAuthMemberPort.fetchMember(serverType, authCode) }
                    verify { mocks.findMemberPort.findMemberByOAuthIdAndServerType(member.oAuthId, serverType) }
                    verify(exactly = 0) { mocks.saveMemberPort.save(member) }
                }
            }
        }
    }
}
