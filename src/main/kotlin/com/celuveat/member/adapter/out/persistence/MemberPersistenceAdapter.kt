package com.celuveat.member.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.member.application.port.out.SaveMemberPort
import com.celuveat.member.domain.Member
import com.celuveat.member.domain.OAuthServerType

@Adapter
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberPersistenceMapper: MemberPersistenceMapper,
) : SaveMemberPort, FindMemberPort {

    override fun findMemberByOAuthIdAndServerType(oauthId: String, serverType: OAuthServerType): Member? {
        return memberJpaRepository.findMemberByOAuthIdAndServerType(oauthId, serverType)?.let {
            memberPersistenceMapper.toDomain(it)
        }
    }

    override fun save(member: Member): Member {
        val memberEntity = memberPersistenceMapper.toEntity(member)
        val saveMember = memberJpaRepository.save(memberEntity)
        return memberPersistenceMapper.toDomain(saveMember)
    }
}
