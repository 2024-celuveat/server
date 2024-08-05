package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityYoutubeContentJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaEntity
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.RestaurantInVideoJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.YoutubeContentJpaEntity
import com.celuveat.celeb.application.port.out.DeleteCelebrityPort
import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveCelebrityPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.celeb.exceptions.AlreadyInterestedCelebrityException
import com.celuveat.celeb.exceptions.NotFoundInterestedCelebrityException
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.utils.throwWhen
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository

@Adapter
class CelebrityPersistenceAdapter(
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityYoutubeContentJpaRepository: CelebrityYoutubeContentJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
    private val restaurantInVideoJpaRepository: RestaurantInVideoJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val celebrityPersistenceMapper: CelebrityPersistenceMapper,
) : FindCelebritiesPort, SaveCelebrityPort, DeleteCelebrityPort {
    override fun findInterestedCelebrities(memberId: Long): List<Celebrity> {
        memberJpaRepository.getById(memberId)
        val celebrities = interestedCelebrityJpaRepository.findAllCelebritiesByMemberId(memberId)
        val celebrityIds = celebrities.map { it.id }
        val youtubeContentsByCelebrity = celebritiesToContentMap(celebrityIds)

        return celebrities.map { celebrityPersistenceMapper.toDomain(it, youtubeContentsByCelebrity[it.id]!!) }
    }

    override fun findVisitedCelebritiesByRestaurants(restaurantIds: List<Long>): Map<Long, List<Celebrity>> {
        val celebritiesWithRestaurant = restaurantInVideoJpaRepository.findVisitedCelebrities(restaurantIds)
        val celebrityIds = celebritiesWithRestaurant.map { it.celebrity.id }
        val youtubeContentsByCelebrity = celebritiesToContentMap(celebrityIds)
        return celebritiesWithRestaurant.groupBy { it.restaurantId } // restaurantId 별로 그룹핑
            .mapValues { (_, visitedCelebrities) -> // visitedCelebrities 를 celebrity 객체로 변환
                visitedCelebrities.map {
                    celebrityPersistenceMapper.toDomain(
                        it.celebrity,
                        youtubeContentsByCelebrity[it.celebrity.id]!!,
                    )
                }.distinct()
            }
    }

    private fun celebritiesToContentMap(celebrityIds: List<Long>): Map<Long, List<YoutubeContentJpaEntity>> =
        celebrityYoutubeContentJpaRepository.findByCelebrityIdIn(celebrityIds)
            .groupBy { it.celebrity.id }
            .mapValues { (_, celebrityYoutubeContents) -> celebrityYoutubeContents.map { it.youtubeContent } }

    override fun saveInterestedCelebrity(celebrityId: Long, memberId: Long) {
        val member = memberJpaRepository.getById(memberId)
        val celebrity = celebrityJpaRepository.getById(celebrityId)
        validateExistence(memberId, celebrityId)
        interestedCelebrityJpaRepository.save(
            InterestedCelebrityJpaEntity(
                celebrity = celebrity,
                member = member,
            )
        )
    }

    private fun validateExistence(
        memberId: Long,
        celebrityId: Long,
    ) = throwWhen(
        interestedCelebrityJpaRepository.existsByMemberIdAndCelebrityId(memberId, celebrityId),
    ) { throw AlreadyInterestedCelebrityException }

    override fun deleteInterestedCelebrity(celebrityId: Long, memberId: Long) {
        interestedCelebrityJpaRepository.findByMemberIdAndCelebrityId(memberId, celebrityId)
            ?.let { interestedCelebrityJpaRepository.delete(it) }
            ?: throw NotFoundInterestedCelebrityException
    }
}
