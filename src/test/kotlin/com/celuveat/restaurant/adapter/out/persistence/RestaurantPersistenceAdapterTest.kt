package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(RestaurantPersistenceAdapter::class, RestaurantPersistenceMapper::class, JpaConfig::class)
@DataJpaTest
class RestaurantPersistenceAdapterTest(
    private val restaurantPersistenceAdapter: RestaurantPersistenceAdapter,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val interestedRestaurantJpaRepository: InterestedRestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
) : FunSpec({

    context("회원이 관심 목록에 추가한 음식점을 조회 한다.") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        val restaurantA = savedRestaurants[0]
        val restaurantB = savedRestaurants[1]

        val imagesA = sut.giveMeBuilder<RestaurantImageJpaEntity>()
            .set(RestaurantImageJpaEntity::id, 0)
            .set(RestaurantImageJpaEntity::restaurant, restaurantA)
            .set(RestaurantImageJpaEntity::isThumbnail, true, 1)
            .sampleList(3)
        val imagesB = sut.giveMeBuilder<RestaurantImageJpaEntity>()
            .set(RestaurantImageJpaEntity::id, 0)
            .set(RestaurantImageJpaEntity::restaurant, restaurantB)
            .set(RestaurantImageJpaEntity::isThumbnail, true, 1)
            .sampleList(2)
        restaurantImageJpaRepository.saveAll(imagesA + imagesB)

        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        interestedRestaurantJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, savedMember)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[0])
                    .sample(),
                sut.giveMeBuilder<InterestedRestaurantJpaEntity>()
                    .set(InterestedRestaurantJpaEntity::member, savedMember)
                    .set(InterestedRestaurantJpaEntity::restaurant, savedRestaurants[1])
                    .sample(),
            ),
        )

        test("추가로 조회할 수 있는 음식점의 여부를 응답한다") {
            // when
            val interestedRestaurantResults = restaurantPersistenceAdapter.findInterestedRestaurants(
                savedMember.id,
                0,
                1,
            )

            // then
            interestedRestaurantResults.size shouldBe 1
            interestedRestaurantResults.hasNext shouldBe true
            interestedRestaurantResults.contents.map { it.id } shouldBe listOf(restaurantB.id)
        }

        test("마지막 음식점을 조회한 경우 hasNext는 false를 응답한다") {
            // when
            val interestedRestaurantResults = restaurantPersistenceAdapter.findInterestedRestaurants(
                savedMember.id,
                0,
                2,
            )

            // then
            interestedRestaurantResults.size shouldBe 2
            interestedRestaurantResults.hasNext shouldBe false
            interestedRestaurantResults.contents.map { it.id } shouldContainInOrder listOf(
                restaurantB.id,
                restaurantA.id
            )
        }
    }
})
