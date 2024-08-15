package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.application.port.out.DeleteInterestedCelebrityPort
import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveInterestedCelebrityPort
import com.celuveat.celeb.domain.InterestedCelebrity
import com.celuveat.celeb.exceptions.NotFoundInterestedCelebrityException
import com.celuveat.common.annotation.Adapter
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import org.springframework.transaction.annotation.Transactional

@Adapter
class InterestedCelebrityPersistenceAdapter(
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val interestedCelebrityPersistenceMapper: InterestedCelebrityPersistenceMapper,
) : ReadInterestedCelebritiesPort, SaveInterestedCelebrityPort, DeleteInterestedCelebrityPort {
    override fun readInterestedCelebrities(memberId: Long): List<InterestedCelebrity> {
        val interestedCelebrities = interestedCelebrityJpaRepository.findAllCelebritiesByMemberId(memberId)
        val celebrityIds = interestedCelebrities.map { it.celebrity.id }
        val youtubeContentsByCelebrity = celebritiesToContentMap(celebrityIds)
        return interestedCelebrities.map {
            interestedCelebrityPersistenceMapper.toDomain(
                it,
                youtubeContentsByCelebrity[it.celebrity.id]!!,
            )
        }
    }

    private fun celebritiesToContentMap(celebrityIds: List<Long>): Map<Long, List<YoutubeContentJpaEntity>> =
        celebrityYoutubeContentJpaRepository.findByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
            .mapValues { (_, celebrityYoutubeContents) -> celebrityYoutubeContents.map { it.youtubeContent } }

    @Transactional
    override fun saveInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ) {
        val member = memberJpaRepository.getById(memberId)
        val celebrity = celebrityJpaRepository.getById(celebrityId)
        val entity = interestedCelebrityPersistenceMapper.toEntity(
            celebrity = celebrity,
            member = member,
        )
        interestedCelebrityJpaRepository.save(entity)
    }

    @Transactional
    override fun deleteInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ) {
        interestedCelebrityJpaRepository.findByMemberIdAndCelebrityId(memberId, celebrityId)
            ?.let { interestedCelebrityJpaRepository.delete(it) }
            ?: throw NotFoundInterestedCelebrityException
    }

    override fun existsInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ): Boolean {
        return interestedCelebrityJpaRepository.existsByMemberIdAndCelebrityId(memberId, celebrityId)
    }
}
