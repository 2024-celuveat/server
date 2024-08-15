package com.celuveat.member.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.member.application.port.out.DeleteMemberPort
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.SocialIdentifier
import org.springframework.transaction.annotation.Transactional

@Adapter
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberPersistenceMapper: MemberPersistenceMapper,
) : SaveMemberPort, ReadMemberPort, DeleteMemberPort {
    override fun findBySocialIdentifier(socialIdentifier: SocialIdentifier): Member? {
        return memberJpaRepository.findMemberBySocialIdAndServerType(
            socialIdentifier.socialId,
            socialIdentifier.serverType,
        )?.let { memberPersistenceMapper.toDomain(it) }
    }

    override fun readById(id: Long): Member {
        val entity = memberJpaRepository.getById(id)
        return memberPersistenceMapper.toDomain(entity)
    }

    @Transactional
    override fun save(member: Member): Member {
        val memberEntity = memberPersistenceMapper.toEntity(member)
        val saveMember = memberJpaRepository.save(memberEntity)
        return memberPersistenceMapper.toDomain(saveMember)
    }

    @Transactional
    override fun deleteById(memberId: Long) {
        memberJpaRepository.deleteById(memberId)
    }
}
