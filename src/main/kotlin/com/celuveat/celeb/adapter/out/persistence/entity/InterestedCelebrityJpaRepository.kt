package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InterestedCelebrityJpaRepository : JpaRepository<InterestedCelebrityJpaEntity, Long> {
    @Query(
        """
        SELECT c
        FROM InterestedCelebrityJpaEntity ic
        JOIN ic.celebrity c
        WHERE ic.member.id = :memberId
    """,
    )
    fun findAllCelebritiesByMemberId(memberId: Long): List<CelebrityJpaEntity>
}
