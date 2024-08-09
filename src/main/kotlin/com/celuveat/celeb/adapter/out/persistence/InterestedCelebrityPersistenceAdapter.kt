package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.application.port.out.DeleteInterestedCelebrityPort
import com.celuveat.celeb.application.port.out.FindInterestedCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveInterestedCelebrityPort
import com.celuveat.celeb.domain.InterestedCelebrity
import com.celuveat.celeb.exceptions.AlreadyInterestedCelebrityException
import com.celuveat.celeb.exceptions.NotFoundInterestedCelebrityException
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.utils.throwWhen
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository

@Adapter
class InterestedCelebrityPersistenceAdapter(
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val interestedCelebrityPersistenceMapper: InterestedCelebrityPersistenceMapper,
) : FindInterestedCelebritiesPort, SaveInterestedCelebrityPort, DeleteInterestedCelebrityPort {
    override fun findInterestedCelebrities(memberId: Long): List<InterestedCelebrity> {
        val interestedCelebrities = interestedCelebrityJpaRepository.findAllCelebritiesByMemberId(memberId)
        val celebrityIds = interestedCelebrities.map { it.celebrity.id }
        val youtubeContentsByCelebrity = celebritiesToContentMap(celebrityIds)
        return interestedCelebrities.map {
            interestedCelebrityPersistenceMapper.toDomain(
                it,
                youtubeContentsByCelebrity[it.id]!!
            )
        }
    }

    private fun celebritiesToContentMap(celebrityIds: List<Long>): Map<Long, List<YoutubeContentJpaEntity>> =
        celebrityYoutubeContentJpaRepository.findByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
            .mapValues { (_, celebrityYoutubeContents) -> celebrityYoutubeContents.map { it.youtubeContent } }

    override fun saveInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ) {
        val member = memberJpaRepository.getById(memberId)
        val celebrity = celebrityJpaRepository.getById(celebrityId)
        validateExistence(memberId, celebrityId)
        interestedCelebrityJpaRepository.save(
            InterestedCelebrityJpaEntity(
                celebrity = celebrity,
                member = member,
            ),
        )
    }

    private fun validateExistence(
        memberId: Long,
        celebrityId: Long,
    ) = throwWhen(
        interestedCelebrityJpaRepository.existsByMemberIdAndCelebrityId(memberId, celebrityId),
    ) { throw AlreadyInterestedCelebrityException }

    override fun deleteInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ) {
        interestedCelebrityJpaRepository.findByMemberIdAndCelebrityId(memberId, celebrityId)
            ?.let { interestedCelebrityJpaRepository.delete(it) }
            ?: throw NotFoundInterestedCelebrityException
    }
}
