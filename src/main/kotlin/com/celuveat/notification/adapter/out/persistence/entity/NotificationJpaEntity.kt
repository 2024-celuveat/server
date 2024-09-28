package com.celuveat.notification.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaEntity
import com.celuveat.notification.domain.NotificationType
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "notification")
@Entity
class NotificationJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val member: MemberJpaEntity,
    @Enumerated(STRING)
    val notificationType: NotificationType,
    val content: String,
    var isRead: Boolean,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
