package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewImagePersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewPersistenceMapper
import com.celuveat.review.domain.Review
import com.celuveat.review.domain.ReviewImage
import com.celuveat.review.domain.Star.FOUR
import com.celuveat.review.domain.Star.ONE
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(
    ReviewPersistenceAdapter::class,
    ReviewPersistenceMapper::class,
    ReviewImagePersistenceMapper::class,
    RestaurantPersistenceMapper::class,
    MemberPersistenceMapper::class,
    JpaConfig::class,
)
@DataJpaTest
class ReviewPersistenceAdapterTest(
    memberPersistenceMapper: MemberPersistenceMapper,
    restaurantPersistenceMapper: RestaurantPersistenceMapper,
    memberJpaRepository: MemberJpaRepository,
    restaurantJpaRepository: RestaurantJpaRepository,
    reviewJpaEntityRepository: ReviewJpaEntityRepository,
    reviewImageJpaEntityRepository: ReviewImageJpaEntityRepository,
    reviewJpaAdapter: ReviewPersistenceAdapter
) : BehaviorSpec({

    Given("리뷰 save 시") {
        val memberEntity = sut.giveMeBuilder(MemberJpaEntity::class.java)
            .setExp(MemberJpaEntity::id, 1L)
            .sample()
        val restaurantEntity = sut.giveMeBuilder(RestaurantJpaEntity::class.java)
            .setExp(RestaurantJpaEntity::id, 1L)
            .sample()
        memberJpaRepository.save(memberEntity)
        restaurantJpaRepository.save(restaurantEntity)
        val member = memberPersistenceMapper.toDomain(memberEntity)
        val restaurant = restaurantPersistenceMapper.toDomainWithoutImage(restaurantEntity)
        val review = Review(
            restaurant = restaurant,
            writer = member,
            content = "hi",
            star = FOUR,
            images = listOf(ReviewImage("1"), ReviewImage("2"))
        )

        When("이미 저장된 리뷰에 대해 save 를 재호출한 경우") {
            val saved = reviewJpaAdapter.save(review)
            saved.update("cc", ONE, listOf(ReviewImage("3"), ReviewImage("2"), ReviewImage("4")))
            reviewJpaAdapter.save(saved)

            Then("리뷰 내용은 업데이트 되고, 기존 이미지도 대체된다") {
                reviewJpaEntityRepository.findAll().size shouldBe 1
                reviewImageJpaEntityRepository.findAll().size shouldBe 3
            }
        }
    }
})
