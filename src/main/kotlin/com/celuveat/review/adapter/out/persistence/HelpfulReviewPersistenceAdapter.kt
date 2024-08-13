package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewPersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaEntityRepository
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.domain.HelpfulReview
import com.celuveat.review.domain.Review

// TODO test
@Adapter
class HelpfulReviewPersistenceAdapter(
    private val helpfulReviewJpaEntityRepository: HelpfulReviewJpaEntityRepository,
    private val helpfulReviewPersistenceMapper: HelpfulReviewPersistenceMapper,
    private val reviewImageJpaEntityRepository: ReviewImageJpaEntityRepository,
) : SaveHelpfulReviewPort, ReadHelpfulReviewPort, DeleteHelpfulReviewPort {
    override fun save(helpfulReview: HelpfulReview): HelpfulReview {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        val saved = helpfulReviewJpaEntityRepository.save(entity)
        val images = reviewImageJpaEntityRepository.findAllByReview(saved.review)
        return helpfulReviewPersistenceMapper.toDomain(saved, images)
    }

    override fun deleteHelpfulReview(helpfulReview: HelpfulReview) {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        helpfulReviewJpaEntityRepository.delete(entity)
    }

    override fun readHelpfulReviewByMemberAndReviews(
        memberId: Long,
        reviews: List<Review>,
    ): List<HelpfulReview> {
        return helpfulReviewJpaEntityRepository.findAllByMemberIdAndReviewId(memberId, reviews.map { it.id })
            .map { helpfulReviewPersistenceMapper.toDomainWithoutImage(it) }
    }

    override fun readByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): HelpfulReview {
        val entity = helpfulReviewJpaEntityRepository.getByMemberIdAndReviewId(reviewId, memberId)
        val images = reviewImageJpaEntityRepository.findAllByReview(entity.review)
        return helpfulReviewPersistenceMapper.toDomain(entity, images)
    }

    override fun existsByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): Boolean {
        return helpfulReviewJpaEntityRepository.existsByReviewIdAndMemberId(reviewId, memberId)
    }
}
