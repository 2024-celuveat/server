package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface InterestedCelebrityJpaRepository : JpaRepository<InterestedCelebrityJpaEntity, Long> {
    @EntityGraph(attributePaths = ["celebrity", "member"])
    fun findAllCelebritiesByMemberId(memberId: Long): List<InterestedCelebrityJpaEntity>

    fun findByMemberIdAndCelebrityId(
        memberId: Long,
        celebrityId: Long,
    ): InterestedCelebrityJpaEntity?

    fun existsByMemberIdAndCelebrityId(
        memberId: Long,
        celebrityId: Long,
    ): Boolean
}
