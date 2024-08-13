package com.celuveat.review.application.port

import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.review.application.port.`in`.command.UpdateReviewCommand
import com.celuveat.review.application.port.`in`.command.WriteReviewCommand
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.DeleteReviewPort
import com.celuveat.review.application.port.out.FindReviewPort
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import com.celuveat.review.domain.HelpfulReview
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.Star.FOUR
import com.celuveat.review.exception.AlreadyClickHelpfulReviewException
import com.celuveat.review.exception.NoAuthorityReviewException
import com.celuveat.review.exception.NotFoundHelpfulReviewException
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify

class ReviewServiceTest : BehaviorSpec({

    val readMemberPort: ReadMemberPort = mockk()
    val readRestaurantPort: ReadRestaurantPort = mockk()
    val findReviewPort: FindReviewPort = mockk()
    val readHelpfulReviewPort: ReadHelpfulReviewPort = mockk()
    val saveHelpfulReviewPort: SaveHelpfulReviewPort = mockk()
    val saveReviewPort: SaveReviewPort = mockk()
    val deleteReviewPort: DeleteReviewPort = mockk()
    val deleteHelpfulReviewPort: DeleteHelpfulReviewPort = mockk()

    val reviewService = ReviewService(
        readMemberPort,
        readRestaurantPort,
        findReviewPort,
        readHelpfulReviewPort,
        saveHelpfulReviewPort,
        saveReviewPort,
        deleteReviewPort,
        deleteHelpfulReviewPort,
    )

    Given("리뷰 작성 시") {
        val member = sut.giveMeOne(Member::class.java)
        val restaurant = sut.giveMeOne(Restaurant::class.java)
        val review = sut.giveMeOne(Review::class.java)
        val command = WriteReviewCommand(1L, 1L, "맛나요", FOUR, listOf("img1", "img2"))
        every { readMemberPort.getById(1L) } returns member
        every { readRestaurantPort.getById(1L) } returns restaurant
        every { saveReviewPort.save(any()) } returns review

        When("회원이 리뷰를 작성하면") {

            val id = reviewService.write(command)

            Then("리뷰가 작성된다.") {
                id shouldBe review.id
                verify(exactly = 1) { saveReviewPort.save(any()) }
            }
        }
    }

    Given("리뷰 수정 시") {

        val member = sut.giveMeOne(Member::class.java)
        val other = sut.giveMeOne(Member::class.java)
        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::writer, member)
            .sample()

        When("해당 리뷰를 작성한 회원이 수정을 시도하면") {

            every { findReviewPort.getById(1L) } returns review
            every { readMemberPort.getById(1L) } returns member
            every { saveReviewPort.save(any()) } returns review
            val command = UpdateReviewCommand(1L, 1L, "맛나요", FOUR, listOf("img1", "img2"))
            reviewService.update(command)

            Then("리뷰가 수정된다.") {
                verify(exactly = 1) { saveReviewPort.save(any()) }
            }
        }

        When("해당 리뷰를 작성하지 않은 회원이 수정을 시도하면") {

            every { findReviewPort.getById(1L) } returns review
            every { readMemberPort.getById(2L) } returns other
            val command = UpdateReviewCommand(2L, 1L, "맛나요", FOUR, listOf("img1", "img2"))
            val exception = shouldThrow<NoAuthorityReviewException> {
                reviewService.update(command)
            }

            Then("예외가 발생한다.") {
                exception.errorMessage shouldBe "해당 리뷰에 대한 권한이 없습니다."
                verify(exactly = 0) { saveReviewPort.save(any()) }
            }
        }
    }

    Given("리뷰 삭제 시") {

        val member = sut.giveMeOne(Member::class.java)
        val other = sut.giveMeOne(Member::class.java)
        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::writer, member)
            .sample()

        When("해당 리뷰를 작성한 회원이 삭제를 시도하면") {

            every { deleteReviewPort.delete(any()) } just Runs
            every { readMemberPort.getById(1L) } returns member
            every { findReviewPort.getById(1L) } returns review
            reviewService.delete(1L, 1L)

            Then("리뷰가 삭제된다.") {

                verify(exactly = 1) { deleteReviewPort.delete(review) }
            }
        }

        When("해당 리뷰를 작성하지 않은 회원이 삭제를 시도하면") {

            every { readMemberPort.getById(2L) } returns other
            every { findReviewPort.getById(1L) } returns review
            val exception = shouldThrow<NoAuthorityReviewException> {
                reviewService.delete(2L, 1L)
            }

            Then("예외가 발생한다.") {
                exception.errorMessage shouldBe "해당 리뷰에 대한 권한이 없습니다."
                verify(exactly = 0) { saveReviewPort.save(any()) }
            }
        }
    }

    Given("리뷰 도움돼요 클릭 시") {

        val member = sut.giveMeOne(Member::class.java)
        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::writer, member)
            .sample()

        When("해당 리뷰에 도움돼요를 처음 클릭하는 거라면") {

            every { readHelpfulReviewPort.existsByReviewAndMember(1L, 1L) } returns false
            every { readMemberPort.getById(1L) } returns member
            every { findReviewPort.getById(1L) } returns review
            every { saveReviewPort.save(any()) } returns review
            every { saveHelpfulReviewPort.save(any()) } returns mockk()
            reviewService.clickHelpfulReview(1L, 1L)

            Then("도움되는 리뷰에 추가된다.") {

                verify(exactly = 1) { saveReviewPort.save(review) }
                verify(exactly = 1) { saveHelpfulReviewPort.save(any()) }
            }
        }

        When("이미 해당 리뷰에 대해 도움돼요를 클릭했다면") {

            every { readHelpfulReviewPort.existsByReviewAndMember(1L, 1L) } returns true
            val exception = shouldThrow<AlreadyClickHelpfulReviewException> {
                reviewService.clickHelpfulReview(1L, 1L)
            }

            Then("예외가 발생한다") {
                exception.errorMessage shouldBe "해당 리뷰에 아미 '도움돼요'를 클릭하였습니다."
            }
        }
    }

    Given("리뷰 도움돼요 제거 시") {
        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::helps, 1)
            .sample()
        val helpfulReview = sut.giveMeBuilder(HelpfulReview::class.java)
            .setExp(HelpfulReview::review, review)
            .sample()

        When("해당 리뷰에 대해 도움돼요 클릭을 한 적이 없다면") {

            every { readHelpfulReviewPort.readByReviewAndMember(1L, 1L) } throws NotFoundHelpfulReviewException
            val exception = shouldThrow<NotFoundHelpfulReviewException> {
                reviewService.deleteHelpfulReview(1L, 1L)
            }

            Then("예외가 발생한다.") {
                exception.errorMessage shouldBe "해당 리뷰에 '도움돼요'를 누르지 않았습니다."
            }
        }

        When("해당 리뷰에 대해 도움돼요 클릭을 한 적이 있다면") {

            every { readHelpfulReviewPort.readByReviewAndMember(1L, 1L) } returns helpfulReview
            every { saveReviewPort.save(any()) } returns review
            every { deleteHelpfulReviewPort.deleteHelpfulReview(any()) } just Runs
            reviewService.deleteHelpfulReview(1L, 1L)

            Then("도움돼요가 제거된다.") {
                review.helps shouldBe 1
                verify(exactly = 1) { saveReviewPort.save(any()) }
                verify(exactly = 1) { deleteHelpfulReviewPort.deleteHelpfulReview(any()) }
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
