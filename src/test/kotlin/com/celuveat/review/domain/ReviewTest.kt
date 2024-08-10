package com.celuveat.review.domain

import com.celuveat.member.domain.Member
import com.celuveat.review.exception.NoAuthorityReviewException
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@DisplayName("리뷰는")
class ReviewTest : BehaviorSpec({

    Given("작성자 검증 시") {

        val writer = sut.giveMeOne(Member::class.java)
        val other = sut.giveMeOne(Member::class.java)
        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::writer, writer)
            .sample()

        When("작성자가 아닌 회원이 검증하면") {

            val exception = shouldThrow<NoAuthorityReviewException> {
                review.validateWriter(other)
            }

            Then("검증에 실패한다.") {
                exception.errorMessage shouldBe "해당 리뷰에 대한 권한이 없습니다."
            }
        }

        When("작성자 검증 시 검증에 성공한다.") {

            shouldNotThrowAny {
                review.validateWriter(writer)
            }
        }
    }

    Given("작성된 리뷰를") {

        val review = sut.giveMeBuilder(Review::class.java)
            .sample()

        When("수정하면") {

            review.update(
                content = "update",
                star = Star.TWO,
                images = listOf("img1", "img2").map { ReviewImage(imageUrl = it) }
            )

            Then("내용이 수정된다.") {
                review.content shouldBe "update"
                review.star shouldBe Star.TWO
                review.images.map { it.imageUrl } shouldBe listOf("img1", "img2")
            }
        }
    }

    Given("리뷰의") {

        val review = sut.giveMeBuilder(Review::class.java)
            .setExp(Review::views, 0)
            .setExp(Review::helps, 0)
            .sample()
        val member = sut.giveMeOne(Member::class.java)

        When("조회수를 증가시키면") {

            review.increaseView()

            Then("조회수가 증가된다.") {

                review.views shouldBe 1
            }
        }

        When("도움돼요를 클릭하면") {

            review.clickHelpful(member)

            Then("도움돼요수가 증가된다.") {
                review.helps shouldBe 1
            }
        }

        When("도움돼요 클릭을 취소하면") {

            val before = review.helps
            review.unClickHelpful()

            Then("도움돼요 수가 감소한다.") {
                review.helps shouldBe before - 1
            }
        }
    }
})
