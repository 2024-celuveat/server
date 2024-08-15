package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaRepository
import com.celuveat.celeb.exceptions.NotFoundCelebrityException
import com.celuveat.celeb.exceptions.NotFoundInterestedCelebrityException
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.exception.NotFoundMemberException
import com.celuveat.support.PersistenceAdapterTest
import com.celuveat.support.sut
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.set
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.dao.DataIntegrityViolationException

@PersistenceAdapterTest
class InterestedCelebrityPersistenceAdapterTest(
    private val celebrityPersistenceAdapter: InterestedCelebrityPersistenceAdapter,
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val youtubeContentJpaRepository: YoutubeContentJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
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
        val celebrities = celebrityPersistenceAdapter.readInterestedCelebrities(savedMember.id)

        // then
        celebrities.size shouldBe 2
        celebrities.forAll { it.celebrity.youtubeContents shouldNotBe null }
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
            shouldThrow<DataIntegrityViolationException> {
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
