package com.celuveat.celeb.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityJpaRepository
import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityPersistenceMapper
import com.celuveat.celeb.adapter.out.persistence.entity.InterestedCelebrityJpaRepository
import com.celuveat.celeb.application.port.out.FindInterestedCelebritiesPort
import com.celuveat.celeb.domain.Celebrity
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.exception.NotFoundMemberException

@Adapter
class CelebrityPersistenceAdapter(
    private val celebrityJpaRepository: CelebrityJpaRepository,
    private val celebrityPersistenceMapper: CelebrityPersistenceMapper,
    private val memberJpaRepository: MemberJpaRepository,
    private val interestedCelebrityJpaRepository: InterestedCelebrityJpaRepository,
) : FindInterestedCelebritiesPort {
    override fun findInterestedCelebrities(memberId: Long): List<Celebrity> {
        memberJpaRepository.findByIdOrThrow(memberId) { NotFoundMemberException }
        val interestedCelebrities = interestedCelebrityJpaRepository.findAllCelebritiesByMemberId(memberId)
        return interestedCelebrities.map { celebrityPersistenceMapper.toDomain(it) }
    }
}