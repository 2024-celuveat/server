package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.VideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaRepository
import com.celuveat.celeb.exceptions.AlreadyInterestedCelebrityException
import com.celuveat.celeb.exceptions.NotFoundCelebrityException
import com.celuveat.celeb.exceptions.NotFoundInterestedCelebrityException
import com.celuveat.common.adapter.out.persistence.JpaConfig
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.exception.NotFoundMemberException
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(CelebrityPersistenceAdapter::class, CelebrityPersistenceMapper::class, JpaConfig::class)
@DataJpaTest
class CelebrityPersistenceAdapterTest(
    private val celebrityPersistenceAdapter: CelebrityPersistenceAdapter,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val videoJpaRepository: VideoJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
) : FunSpec({
    test("회원이 관심 목록에 추가한 셀럽을 조회 한다.") {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .sampleList(2)
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::id, 0)
            .set(YoutubeContentJpaEntity::channelId, "@channelId")
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(contentA + contentB)
        celebrityYoutubeContentJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityA)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[0])
                    .sample(),
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityA)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[1])
                    .sample(),
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityB)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[2])
                    .sample(),
            ),
        )
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        interestedCelebrityJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, celebrityA)
                    .sample(),
                sut.giveMeBuilder<InterestedCelebrityJpaEntity>()
                    .set(InterestedCelebrityJpaEntity::member, savedMember)
                    .set(InterestedCelebrityJpaEntity::celebrity, celebrityB)
                    .sample(),
            ),
        )

        // when
        val celebrities = celebrityPersistenceAdapter.findInterestedCelebrities(savedMember.id)

        // then
        assertSoftly {
            celebrities.size shouldBe 2
            celebrities.forAll { it.youtubeContents shouldNotBe null }
        }
    }

    test("식당을 방문한 셀럽을 조회 한다.") {
        // given
        val savedCelebrities = celebrityJpaRepository.saveAll(sut.giveMeBuilder<CelebrityJpaEntity>().sampleList(2))
        val celebrityA = savedCelebrities[0]
        val celebrityB = savedCelebrities[1]

        val contentA = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelAId")
            .sample()
        val contentB = sut.giveMeBuilder<YoutubeContentJpaEntity>()
            .set(YoutubeContentJpaEntity::channelId, "@channelBId")
            .sample()
        val savedContents = youtubeContentJpaRepository.saveAll(listOf(contentA, contentB))
        celebrityYoutubeContentJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityA)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[0])
                    .sample(),
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityA)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[1])
                    .sample(),
                sut.giveMeBuilder<CelebrityYoutubeContentJpaEntity>()
                    .set(CelebrityYoutubeContentJpaEntity::celebrity, celebrityB)
                    .set(CelebrityYoutubeContentJpaEntity::youtubeContent, savedContents[0])
                    .sample(),
            ),
        ) // [셀럽A] -> [컨텐츠A, 컨텐츠B], [셀럽B] -> [컨텐츠A] 에 출연함

        val savedVideos = videoJpaRepository.saveAll(
            listOf(
                generateVideoWithYoutubeContent(savedContents[0]).sample(),
                generateVideoWithYoutubeContent(savedContents[1]).sample(),
                generateVideoWithYoutubeContent(savedContents[1]).sample(),
            ),
        ) // [영상1] -> [컨텐츠A], [영상2, 영상3] -> [컨텐츠B] 의 영상

        val restaurants = restaurantJpaRepository.saveAll(sut.giveMeBuilder<RestaurantJpaEntity>().sampleList(2))
        restaurantInVideoJpaRepository.saveAll(
            listOf(
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[0])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[1])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[0])
                    .sample(),
                sut.giveMeBuilder<RestaurantInVideoJpaEntity>()
                    .set(RestaurantInVideoJpaEntity::video, savedVideos[2])
                    .set(RestaurantInVideoJpaEntity::restaurant, restaurants[1])
                    .sample(),
            ),
        ) // [영상1, 영상2] -> [음식점1], [영상3] -> [음식점2] 에 방문함

        // when
        val visitedCelebritiesByRestaurants =
            celebrityPersistenceAdapter.findVisitedCelebritiesByRestaurants(restaurants.map { it.id })

        // then
        assertSoftly {
            visitedCelebritiesByRestaurants.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[0].id]!!.size shouldBe 2
            visitedCelebritiesByRestaurants[restaurants[1].id]!!.size shouldBe 1
        }
    }

    context("관심 셀럽 등록 시") {
        // given
        val savedCelebrity = celebrityJpaRepository.save(sut.giveMeBuilder<CelebrityJpaEntity>().sample())
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())
        test("관심 셀럽을 등록한다.") {
            // when & then
            shouldNotThrowAny {
                celebrityPersistenceAdapter.saveInterestedCelebrity(savedCelebrity.id, savedMember.id)
            }
        }

        test("이미 관심 셀럽으로 등록한 경우 예외를 발생시킨다.") {
            // when & then
            shouldThrow<AlreadyInterestedCelebrityException> {
                celebrityPersistenceAdapter.saveInterestedCelebrity(savedCelebrity.id, savedMember.id)
                celebrityPersistenceAdapter.saveInterestedCelebrity(savedCelebrity.id, savedMember.id)
            }
        }

        test("존재 하지 않는 회원인 경우 예외를 발생시킨다.") {
            // when & then
            shouldThrow<NotFoundMemberException> {
                celebrityPersistenceAdapter.saveInterestedCelebrity(savedCelebrity.id, 0)
            }
        }

        test("존재 하지 않는 셀럽인 경우 예외를 발생시킨다.") {
            // when & then
            shouldThrow<NotFoundCelebrityException> {
                celebrityPersistenceAdapter.saveInterestedCelebrity(0, savedMember.id)
            }
        }
    }

    context("관심 셀럽 삭제 시") {
        // given
        val savedCelebrity = celebrityJpaRepository.save(sut.giveMeBuilder<CelebrityJpaEntity>().sample())
        val savedMember = memberJpaRepository.save(sut.giveMeOne<MemberJpaEntity>())

        test("관심 셀럽을 삭제한다.") {
            interestedCelebrityJpaRepository.save(
                InterestedCelebrityJpaEntity(
                    member = savedMember,
                    celebrity = savedCelebrity,
                ),
            )

            // when & then
            shouldNotThrowAny {
                celebrityPersistenceAdapter.deleteInterestedCelebrity(savedCelebrity.id, savedMember.id)
            }
        }

        test("관심 셀럽이 존재하지 않는 경우 예외를 발생시킨다.") {
            // when & then
            shouldThrow<NotFoundInterestedCelebrityException> {
                celebrityPersistenceAdapter.deleteInterestedCelebrity(savedCelebrity.id, savedMember.id)
            }
        }
    }
})

private fun generateVideoWithYoutubeContent(youtubeContent: YoutubeContentJpaEntity) =
    sut.giveMeBuilder<VideoJpaEntity>().set(VideoJpaEntity::youtubeContent, youtubeContent)
