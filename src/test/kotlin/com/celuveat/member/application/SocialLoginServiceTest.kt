package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.GetSocialLoginUrlPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import com.celuveat.member.domain.SocialLoginType
import com.celuveat.support.sut
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

class SocialLoginServiceTest : BehaviorSpec({

    val saveMemberPort: SaveMemberPort = mockk()
    val findMemberPort: FindMemberPort = mockk()
    val fetchSocialMemberPort: FetchSocialMemberPort = mockk()
    val getSocialLoginUrlPort: GetSocialLoginUrlPort = mockk()

    val socialLoginService = SocialLoginService(
        fetchSocialMemberPort = fetchSocialMemberPort,
        saveMemberPort = saveMemberPort,
        findMemberPort = findMemberPort,
        getSocialLoginUrlPort = getSocialLoginUrlPort,
    )

    Given("소셜 로그인을 통해 회원가입할 때") {

        val serverType = SocialLoginType.KAKAO
        val socialIdentifier = SocialIdentifier(serverType = serverType, socialId = "socialId")
        val redirectUrl = "redirectUrl"
        val authCode = "authCode"

        val member = sut.giveMeBuilder<Member>()
            .set(Member::id, 0L)
            .set(Member::socialIdentifier, socialIdentifier)
            .sample()
        val savedMember = member.copy(id = 1L)

        When("최초 회원인 경우") {
            every { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) } returns member
            every { findMemberPort.findBySocialIdentifier(socialIdentifier) } returns null
            every { saveMemberPort.save(member) } returns savedMember
            val command = SocialLoginCommand(serverType, authCode, redirectUrl)

            val result = socialLoginService.login(command)
            Then("회원가입이 완료된다") {
                result shouldBe 1L

                verify { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) }
                verify { findMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify { saveMemberPort.save(member) }
            }
        }

        When("이미 가입된 회원인 경우") {
            every { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) } returns member
            every { findMemberPort.findBySocialIdentifier(socialIdentifier) } returns savedMember
            val command = SocialLoginCommand(serverType, authCode, redirectUrl)

            val result = socialLoginService.login(command)
            Then("로그인이 완료된다") {
                result shouldBe savedMember.id

                verify { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) }
                verify { findMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify(exactly = 0) { saveMemberPort.save(member) }
            }
        }
    }

    Given("소셜 로그인 URL을 조회할 때") {
        val serverType = SocialLoginType.KAKAO
        val redirectUrl = "redirectUrl"
        val socialLoginUrl = "https://social.com/authorize?redirect_uri=$redirectUrl&client_id=clientId"
        every { getSocialLoginUrlPort.getSocialLoginUrl(serverType, redirectUrl) } returns socialLoginUrl
        When("소셜 로그인 타입과 리다이렉트될 URL을 전달하면") {
            val result = socialLoginService.getSocialLoginUrl(serverType, redirectUrl)

            Then("소셜 로그인 URL이 반환된다") {
                result shouldBe socialLoginUrl

                verify { getSocialLoginUrlPort.getSocialLoginUrl(serverType, redirectUrl) }
            }
        }
    }
}) {
    override suspend fun afterEach(
        testCase: TestCase,
        result: TestResult,
    ) {
        clearAllMocks()
        unmockkAll()
    }
}
