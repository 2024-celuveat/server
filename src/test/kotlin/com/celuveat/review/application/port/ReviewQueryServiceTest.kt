package com.celuveat.review.application.port

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.application.ReviewQueryService
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.ReadReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import com.celuveat.review.domain.HelpfulReview
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.Star
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll

class ReviewQueryServiceTest : BehaviorSpec({

    val readReviewPort: ReadReviewPort = mockk()
    val readHelpfulReviewPort: ReadHelpfulReviewPort = mockk()
    val saveReviewPort: SaveReviewPort = mockk()

    val reviewQueryService = ReviewQueryService(
        readReviewPort,
        readHelpfulReviewPort,
        saveReviewPort,
    )

    Given("단일 리뷰 조회 시") {
        val memberId = 1L
        val reviewId = 1L
        val review = sut.giveMeBuilder<Review>()
            .set(Review::id, reviewId)
            .set(Review::views, 0)
            .sample()
        When("회원이 리뷰를 조회하면") {
            val viewCount = review.views
            every { readReviewPort.readById(reviewId) } returns review
            every { saveReviewPort.save(review) } returns review
            every { readHelpfulReviewPort.existsByReviewAndMember(reviewId, memberId) } returns true

            reviewQueryService.read(memberId, reviewId)

            Then("조회수가 증가하고, 도움돼요를 클릭 여부가 반환 된다.") {
                review.views shouldBe viewCount + 1
                reviewQueryService.read(memberId, reviewId).clickedHelpful shouldBe true
            }
        }

        When("비회원이 리뷰를 조회하면") {
            val viewCount = review.views
            every { readReviewPort.readById(reviewId) } returns review
            every { saveReviewPort.save(review) } returns review

            reviewQueryService.read(null, reviewId)

            Then("조회수가 증가하고, 도움돼요 클릭 여부는 false로 반환 된다.") {
                review.views shouldBe viewCount + 1
            }
        }
    }

    Given("음식점의 리뷰 조회 시") {
        val memberId = 1L
        val restaurantId = 1L
        val page = 1
        val size = 3
        val reviews = sut.giveMeBuilder<Review>()
            .setExp(Review::star, Star.FOUR)
            .sampleList(3)
        val helpFulReview = sut.giveMeBuilder<HelpfulReview>()
            .setExp(HelpfulReview::review, reviews[0])
            .sample()
        val reviewResults = SliceResult.of(reviews, page, false)
        When("회원이 음식점의 리뷰를 조회하면") {
            every { readReviewPort.readAllByRestaurantId(restaurantId, page, size) } returns reviewResults
            every {
                readHelpfulReviewPort.readHelpfulReviewByMemberAndReviews(
                    memberId,
                    reviewResults.contents,
                )
            } returns listOf(helpFulReview)

            val result = reviewQueryService.readAll(memberId, restaurantId, page, size)
            Then("리뷰 목록의 도움돼요 클릭 여부가 반환 된다.") {
                result.contents.size shouldBe reviewResults.contents.size
                result.contents.filter { it.clickedHelpful }.size shouldBe 1
            }
        }

        When("비회원이 음식점의 리뷰를 조회하면") {
            every { readReviewPort.readAllByRestaurantId(restaurantId, page, size) } returns reviewResults

            val result = reviewQueryService.readAll(null, restaurantId, page, size)
            Then("리뷰 목록의 도움돼요 클릭 여부는 false로 반환 된다.") {
                result.contents.size shouldBe reviewResults.contents.size
                result.contents.filter { it.clickedHelpful }.size shouldBe 0
            }
        }
    }

    Given("내가 작성한 리뷰 조회 시") {
        val memberId = 1L
        val reviews = sut.giveMeBuilder<Review>()
            .setExp(Review::star, Star.FOUR)
            .sampleList(3)
        val helpFulReview = sut.giveMeBuilder<HelpfulReview>()
            .setExp(HelpfulReview::review, reviews[0])
            .sample()
        When("내가 작성한 리뷰를 조회하면") {
            every { readReviewPort.readMyReviews(memberId) } returns reviews
            every {
                readHelpfulReviewPort.readHelpfulReviewByMemberAndReviews(
                    memberId,
                    reviews,
                )
            } returns listOf(helpFulReview)

            val result = reviewQueryService.readMyReviews(memberId)
            Then("리뷰가 조회되고, 도움돼요 클릭 여부가 반환 된다.") {
                result.size shouldBe reviews.size
                result.filter { it.clickedHelpful }.size shouldBe 1
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
