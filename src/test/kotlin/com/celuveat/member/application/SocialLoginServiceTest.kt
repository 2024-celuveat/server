package com.celuveat.member.application

import com.celuveat.member.application.port.`in`.command.SocialLoginCommand
import com.celuveat.member.application.port.`in`.command.WithdrawSocialLoginCommand
import com.celuveat.member.application.port.out.DeleteMemberPort
import com.celuveat.member.application.port.out.FetchSocialMemberPort
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.member.application.port.out.ReadSocialLoginUrlPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.application.port.out.WithdrawSocialMemberPort
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
    val readMemberPort: ReadMemberPort = mockk()
    val fetchSocialMemberPort: FetchSocialMemberPort = mockk()
    val readSocialLoginUrlPort: ReadSocialLoginUrlPort = mockk()
    val withdrawSocialMemberPort: WithdrawSocialMemberPort = mockk()
    val deleteMemberPort: DeleteMemberPort = mockk()

    val socialLoginService = SocialLoginService(
        fetchSocialMemberPort = fetchSocialMemberPort,
        saveMemberPort = saveMemberPort,
        readMemberPort = readMemberPort,
        readSocialLoginUrlPort = readSocialLoginUrlPort,
        withdrawSocialMemberPort = withdrawSocialMemberPort,
        deleteMemberPort = deleteMemberPort,
    )

    Given("소셜 로그인을 통해 회원가입할 때") {

        val serverType = SocialLoginType.KAKAO
        val socialIdentifier = SocialIdentifier(serverType = serverType, socialId = "socialId", "refreshToken")
        val redirectUrl = "redirectUrl"
        val authCode = "authCode"

        When("최초 회원인 경우") {
            val member = sut.giveMeBuilder<Member>()
                .set(Member::id, 0L)
                .set(Member::socialIdentifier, socialIdentifier)
                .sample()
            val savedMember = sut.giveMeBuilder(member)
                .set(Member::id, 1L)
                .sample()

            every { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) } returns member
            every { readMemberPort.findBySocialIdentifier(socialIdentifier) } returns null
            every { saveMemberPort.save(any()) } returns savedMember
            val command = SocialLoginCommand(serverType, authCode, redirectUrl)

            val result = socialLoginService.login(command)
            Then("회원가입이 완료된다") {
                result shouldBe 1L

                verify { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) }
                verify { readMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify(exactly = 2) { saveMemberPort.save(any()) }
            }
        }

        When("이미 가입된 회원인 경우") {
            val member = sut.giveMeBuilder<Member>()
                .set(Member::id, 0L)
                .set(Member::socialIdentifier, socialIdentifier)
                .sample()
            val savedMember = sut.giveMeBuilder(member)
                .set(Member::id, 1L)
                .sample()

            every { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) } returns member
            every { readMemberPort.findBySocialIdentifier(socialIdentifier) } returns savedMember
            every { saveMemberPort.save(any()) } returns savedMember
            val command = SocialLoginCommand(serverType, authCode, redirectUrl)

            val result = socialLoginService.login(command)
            Then("로그인이 완료된다") {
                result shouldBe savedMember.id

                verify { fetchSocialMemberPort.fetchMember(serverType, authCode, redirectUrl) }
                verify { readMemberPort.findBySocialIdentifier(socialIdentifier) }
                verify(exactly = 1) { saveMemberPort.save(any()) }
            }
        }
    }

    Given("소셜 로그인 URL을 조회할 때") {
        val serverType = SocialLoginType.KAKAO
        val redirectUrl = "redirectUrl"
        val socialLoginUrl = "https://social.com/authorize?redirect_uri=$redirectUrl&client_id=clientId"
        every { readSocialLoginUrlPort.readSocialLoginUrl(serverType, redirectUrl) } returns socialLoginUrl
        When("소셜 로그인 타입과 리다이렉트될 URL을 전달하면") {
            val result = socialLoginService.getSocialLoginUrl(serverType, redirectUrl)

            Then("소셜 로그인 URL이 반환된다") {
                result shouldBe socialLoginUrl

                verify { readSocialLoginUrlPort.readSocialLoginUrl(serverType, redirectUrl) }
            }
        }
    }

    Given("소셜 로그인 회원 탈퇴 시") {
        val serverType = SocialLoginType.KAKAO
        val redirectUrl = "redirectUrl"
        val authCode = "authCode"
        val command = WithdrawSocialLoginCommand(1L, redirectUrl)
        val refreshToken = "refreshToken"
        val member = Member(
            id = command.memberId,
            nickname = "nickname",
            profileImageUrl = "profileImageUrl",
            email = "email",
            socialIdentifier = SocialIdentifier(serverType, "socialId", refreshToken),
        )
        every { readMemberPort.readById(command.memberId) } returns member
        every { withdrawSocialMemberPort.withdraw(refreshToken, serverType, redirectUrl) } returns Unit
        every { deleteMemberPort.deleteById(command.memberId) } returns Unit
        When("회원 탈퇴 요청을 전달하면") {
            socialLoginService.withdraw(command)

            Then("소셜 로그인 회원 탈퇴가 완료된다") {
                verify { withdrawSocialMemberPort.withdraw(refreshToken, serverType, redirectUrl) }
                verify { deleteMemberPort.deleteById(command.memberId) }
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
