package com.celuveat.review.adapter.out.persistence

import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaEntity
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewJpaEntity
import com.celuveat.review.adapter.out.persistence.entity.ReviewJpaRepository
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.ReviewImage
import com.celuveat.review.domain.Star.FOUR
import com.celuveat.review.domain.Star.ONE
import com.celuveat.review.exception.NotFoundReviewException
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@PersistenceAdapterTest
class ReviewPersistenceAdapterTest(
    memberPersistenceMapper: MemberPersistenceMapper,
    restaurantPersistenceMapper: RestaurantPersistenceMapper,
    memberJpaRepository: MemberJpaRepository,
    restaurantJpaRepository: RestaurantJpaRepository,
    reviewJpaRepository: ReviewJpaRepository,
    reviewImageJpaRepository: ReviewImageJpaRepository,
    reviewPersistenceAdapter: ReviewPersistenceAdapter,
) : BehaviorSpec({

    Given("리뷰 저장 시") {
        val memberEntity = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val restaurantEntity = restaurantJpaRepository.save(sut.giveMeOne(RestaurantJpaEntity::class.java))
        val member = memberPersistenceMapper.toDomain(memberEntity)
        val restaurant = restaurantPersistenceMapper.toDomainWithoutImage(restaurantEntity)
        val review = Review(
            restaurant = restaurant,
            writer = member,
            content = "hi",
            star = FOUR,
            images = listOf(ReviewImage("1"), ReviewImage("2")),
        )

        When("이미 저장된 리뷰에 대해 save 를 재호출한 경우") {
            val saved = reviewPersistenceAdapter.save(review)
            saved.update("cc", ONE, listOf(ReviewImage("3"), ReviewImage("2"), ReviewImage("4")))
            reviewPersistenceAdapter.save(saved)

            Then("리뷰 내용은 업데이트 되고, 기존 이미지도 대체된다") {
                reviewJpaRepository.findAll().size shouldBe 1
                reviewImageJpaRepository.findAll().size shouldBe 3
            }
        }
    }

    Given("리뷰 삭제 시") {
        val memberEntity = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val restaurantEntity = restaurantJpaRepository.save(sut.giveMeOne(RestaurantJpaEntity::class.java))
        val member = memberPersistenceMapper.toDomain(memberEntity)
        val restaurant = restaurantPersistenceMapper.toDomainWithoutImage(restaurantEntity)
        val review = Review(
            restaurant = restaurant,
            writer = member,
            content = "hi",
            star = FOUR,
            images = listOf(ReviewImage("1"), ReviewImage("2")),
        )
        val saved = reviewPersistenceAdapter.save(review)

        When("리뷰를 삭제한 경우") {
            reviewPersistenceAdapter.delete(saved)

            Then("리뷰가 삭제된다") {
                reviewJpaRepository.findById(saved.id).isEmpty shouldBe true
            }
        }
    }

    Given("단일 리뷰 조회 시") {
        val memberEntity = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val restaurantEntity = restaurantJpaRepository.save(sut.giveMeOne(RestaurantJpaEntity::class.java))
        val member = memberPersistenceMapper.toDomain(memberEntity)
        val restaurant = restaurantPersistenceMapper.toDomainWithoutImage(restaurantEntity)
        val review = Review(
            restaurant = restaurant,
            writer = member,
            content = "hi",
            star = FOUR,
            images = listOf(ReviewImage("1"), ReviewImage("2")),
        )

        When("리뷰가 존재하는 경우") {
            val saved = reviewPersistenceAdapter.save(review)
            val found = reviewPersistenceAdapter.readById(saved.id)

            Then("리뷰가 조회된다") {
                found.id shouldBe saved.id
            }
        }

        When("리뷰가 존재하지 않는 경우") {
            val exception = shouldThrow<NotFoundReviewException> { reviewPersistenceAdapter.readById(0) }

            Then("NotFoundReviewException 예외가 발생한다") {
                exception.message shouldBe "존재 하지 않는 리뷰입니다."
            }
        }
    }

    Given("음식점에 달린 리뷰 조회 시") {
        val member = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val restaurant = restaurantJpaRepository.save(sut.giveMeOne(RestaurantJpaEntity::class.java))
        val reviews = sut.giveMeBuilder<ReviewJpaEntity>()
            .setExp(ReviewJpaEntity::restaurant, restaurant)
            .setExp(ReviewJpaEntity::writer, member)
            .setExp(ReviewJpaEntity::star, 4)
            .sampleList(3)
        val savedReviews = reviewJpaRepository.saveAll(reviews)
        val reviewAImages = sut.giveMeBuilder<ReviewImageJpaEntity>()
            .setExp(ReviewImageJpaEntity::review, savedReviews[0])
            .sampleList(2)
        val reviewBImages = sut.giveMeBuilder<ReviewImageJpaEntity>()
            .setExp(ReviewImageJpaEntity::review, savedReviews[1])
            .sampleList(3)
        reviewImageJpaRepository.saveAll(reviewAImages + reviewBImages)

        When("음식점에 달린 리뷰를 조회한 경우") {
            val sliceResult = reviewPersistenceAdapter.readAllByRestaurantId(restaurant.id, 0, 1)

            Then("리뷰가 조회된다") {
                sliceResult.contents.size shouldBe 1
                sliceResult.hasNext shouldBe true
            }
        }
    }

    Given("내가 작성한 리뷰 조회 시") {
        val member = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val otherMember = memberJpaRepository.save(sut.giveMeOne(MemberJpaEntity::class.java))
        val restaurant = restaurantJpaRepository.save(sut.giveMeOne(RestaurantJpaEntity::class.java))
        val myReview = sut.giveMeBuilder<ReviewJpaEntity>()
            .setExp(ReviewJpaEntity::restaurant, restaurant)
            .setExp(ReviewJpaEntity::writer, member)
            .setExp(ReviewJpaEntity::star, 4)
            .sample()

        val otherReview = sut.giveMeBuilder<ReviewJpaEntity>()
            .setExp(ReviewJpaEntity::restaurant, restaurant)
            .setExp(ReviewJpaEntity::writer, otherMember)
            .setExp(ReviewJpaEntity::star, 4)
            .sample()
        val savedReviews = reviewJpaRepository.saveAll(listOf(myReview, otherReview))
        val reviewAImages = sut.giveMeBuilder<ReviewImageJpaEntity>()
            .setExp(ReviewImageJpaEntity::review, savedReviews[0])
            .sampleList(2)
        val reviewBImages = sut.giveMeBuilder<ReviewImageJpaEntity>()
            .setExp(ReviewImageJpaEntity::review, savedReviews[1])
            .sampleList(3)
        reviewImageJpaRepository.saveAll(reviewAImages + reviewBImages)

        When("내가 작성한 리뷰를 조회한 경우") {
            val myReviews = reviewPersistenceAdapter.readMyReviews(member.id)

            Then("내가 작성한 리뷰만 조회된다") {
                myReviews.size shouldBe 1
            }
        }
    }
})
