package com.celuveat.member.application.port.out

interface DeleteMemberPort {
    fun deleteById(memberId: Long)
}
