package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.exception.NotFoundMemberException
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.exception.NotFoundInterestedRestaurantException
import com.celuveat.restaurant.exception.NotFoundRestaurantException
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.springframework.dao.DataIntegrityViolationException

private const val NOT_EXIST_ID = -1L

@PersistenceAdapterTest
class InterestedRestaurantPersistenceAdapterTest(
    private val restaurantPersistenceAdapter: InterestedRestaurantPersistenceAdapter,
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
            val interestedRestaurantResults = restaurantPersistenceAdapter.readInterestedRestaurants(
                savedMember.id,
                0,
                1,
            )

            // then
            interestedRestaurantResults.size shouldBe 1
            interestedRestaurantResults.hasNext shouldBe true
            interestedRestaurantResults.contents.map { it.restaurant.id } shouldBe listOf(restaurantB.id)
        }

        test("마지막 음식점을 조회한 경우 hasNext는 false를 응답한다") {
            // when
            val interestedRestaurantResults = restaurantPersistenceAdapter.readInterestedRestaurants(
                savedMember.id,
                0,
                2,
            )

            // then
            interestedRestaurantResults.size shouldBe 2
            interestedRestaurantResults.hasNext shouldBe false
            interestedRestaurantResults.contents.map { it.restaurant.id } shouldContainInOrder listOf(
                restaurantB.id,
                restaurantA.id,
            )
        }
    }

    context("관심 음식점 등록 시") {
        test("관심 음식점을 등록한다.") {
            // given
            val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
            val savedRestaurant = restaurantJpaRepository.save(sut.giveMeOne<RestaurantJpaEntity>())

            // when & then
            shouldNotThrowAny {
                restaurantPersistenceAdapter.saveInterestedRestaurant(
                    savedMember.id,
                    savedRestaurant.id,
                )
            }
        }

        test("존재 하지 않는 회원인 경우 예외가 발생한다.") {
            // given
            val savedRestaurant = restaurantJpaRepository.save(sut.giveMeOne<RestaurantJpaEntity>())

            // when & then
            shouldThrow<NotFoundMemberException> {
                restaurantPersistenceAdapter.saveInterestedRestaurant(NOT_EXIST_ID, savedRestaurant.id)
            }
        }

        test("존재 하지 않는 음식점인 경우 예외가 발생한다.") {
            // given
            val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())

            // when & then
            shouldThrow<NotFoundRestaurantException> {
                restaurantPersistenceAdapter.saveInterestedRestaurant(savedMember.id, NOT_EXIST_ID)
            }
        }

        test("이미 등록된 관심 음식점인 경우 예외가 발생한다.") {
            // given
            val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
            val savedRestaurant = restaurantJpaRepository.save(sut.giveMeOne<RestaurantJpaEntity>())
            restaurantPersistenceAdapter.saveInterestedRestaurant(savedMember.id, savedRestaurant.id)

            // when & then
            shouldThrow<DataIntegrityViolationException> {
                restaurantPersistenceAdapter.saveInterestedRestaurant(savedMember.id, savedRestaurant.id)
            }
        }
    }

    context("관심 음식점 삭제 시") {
        // given
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        val savedRestaurant = restaurantJpaRepository.save(sut.giveMeOne<RestaurantJpaEntity>())
        interestedRestaurantJpaRepository.save(
            InterestedRestaurantJpaEntity(
                member = savedMember,
                restaurant = savedRestaurant,
            ),
        )

        test("관심 음식점을 삭제한다.") {
            // when & then
            shouldNotThrowAny {
                restaurantPersistenceAdapter.deleteInterestedRestaurant(
                    savedMember.id,
                    savedRestaurant.id,
                )
            }
        }

        test("존재하지 않는 관심 음식점인 경우 예외를 발생한다.") {
            // when & then
            shouldThrow<NotFoundInterestedRestaurantException> {
                restaurantPersistenceAdapter.deleteInterestedRestaurant(savedMember.id, NOT_EXIST_ID)
            }
        }
    }

    context("음식점 id로 관심 음식점 조회 시") {
        // given
        val savedRestaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(3))
        val restaurantA = savedRestaurants[0]
        val restaurantB = savedRestaurants[1]
        val restaurantC = savedRestaurants[2]

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

        test("음식점 id로 관심 음식점을 조회한다.") {
            // when
            val restaurantIds = savedRestaurants.map { it.id }
            val interestedRestaurants = restaurantPersistenceAdapter.readInterestedRestaurantsByIds(
                savedMember.id,
                restaurantIds,
            )

            // then
            interestedRestaurants.size shouldBe 2
            interestedRestaurants.map { it.restaurant.id } shouldContainAll listOf(restaurantA.id, restaurantB.id)
            interestedRestaurants.map { it.restaurant.id } shouldNotContain listOf(restaurantC.id)
        }
    }
})
