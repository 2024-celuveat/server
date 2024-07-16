package com.celuveat.member.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier

@Adapter
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberPersistenceMapper: MemberPersistenceMapper,
) : SaveMemberPort, FindMemberPort {

    override fun findBySocialIdentifier(socialIdentifier: SocialIdentifier): Member? {
        return memberJpaRepository.findMemberBySocialIdAndServerType(
            socialIdentifier.socialId,
            socialIdentifier.serverType
        )?.let { memberPersistenceMapper.toDomain(it) }
    }

    override fun save(member: Member): Member {
        val memberEntity = memberPersistenceMapper.toEntity(member)
        val saveMember = memberJpaRepository.save(memberEntity)
        return memberPersistenceMapper.toDomain(saveMember)
    }
}
