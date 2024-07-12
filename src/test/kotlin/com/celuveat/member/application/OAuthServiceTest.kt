package com.celuveat.member.application

import com.celuveat.member.application.port.out.FetchOAuthMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.support.fixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify

class OAuthServiceTest : BehaviorSpec({

    val saveMemberPort: SaveMemberPort = mockk()
    val findMemberPort: FindMemberPort = mockk()
    val fetchOAuthMemberPort: FetchOAuthMemberPort = mockk()

    val oAuthService = OAuthService(
        fetchOAuthMemberPort = fetchOAuthMemberPort,
        saveMemberPort = saveMemberPort,
        findMemberPort = findMemberPort
    )

    Given("소셜 로그인을 통해 회원가입할 때") {

        val serverType = OAuthServerType.KAKAO
        val socialIdentifier = SocialIdentifier(serverType = serverType, oAuthId = "oAuthId")
        val authCode = "authCode"

        val member = fixtureMonkey.giveMeBuilder<Member>()
            .set(Member::id, 0L)
            .set(Member::socialIdentifier, socialIdentifier)
            .sample()
        val savedMember = member.copy(id = 1L)

        When("최초 회원인 경우") {
            every { fetchOAuthMemberPort.fetchMember(serverType, authCode) } returns member
            every { findMemberPort.findBySocialIdentifier(socialIdentifier) } returns null
            every { saveMemberPort.save(member) } returns savedMember

            val result = oAuthService.login(serverType, authCode)

            Then("회원가입이 완료된다") {
                result shouldBe 1L

                verify { fetchOAuthMemberPort.fetchMember(serverType, authCode) }
                verify { findMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify { saveMemberPort.save(member) }
            }
        }

        When("이미 가입된 회원인 경우") {
            every { fetchOAuthMemberPort.fetchMember(serverType, authCode) } returns member
            every { findMemberPort.findBySocialIdentifier(socialIdentifier) } returns savedMember

            val result = oAuthService.login(serverType, authCode)

            Then("로그인이 완료된다") {
                result shouldBe savedMember.id

                verify { fetchOAuthMemberPort.fetchMember(serverType, authCode) }
                verify { findMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify(exactly = 0) { saveMemberPort.save(member) }
            }
        }
    }
}) {

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
        unmockkAll()
    }
}
