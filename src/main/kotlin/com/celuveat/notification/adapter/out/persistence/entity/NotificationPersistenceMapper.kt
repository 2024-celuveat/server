package com.celuveat.notification.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.notification.domain.Notification

@Mapper
class NotificationPersistenceMapper(
    private val memberPersistenceMapper: MemberPersistenceMapper,
) {
    fun toDomain(notification: NotificationJpaEntity): Notification {
        return Notification(
            id = notification.id,
            notificationType = notification.notificationType,
            member = memberPersistenceMapper.toDomain(notification.member),
            content = notification.content,
            isRead = notification.isRead,
            createdAt = notification.createdAt,
        )
    }

    fun toEntity(notification: Notification): NotificationJpaEntity {
        return NotificationJpaEntity(
            id = notification.id,
            notificationType = notification.notificationType,
            member = memberPersistenceMapper.toEntity(notification.member),
            content = notification.content,
            isRead = notification.isRead,
        )
    }
}
