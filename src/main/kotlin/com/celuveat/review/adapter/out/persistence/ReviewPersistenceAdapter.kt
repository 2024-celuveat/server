package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewImagePersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewJpaRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewPersistenceMapper
import com.celuveat.review.application.port.out.DeleteReviewPort
import com.celuveat.review.application.port.out.ReadReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import com.celuveat.review.domain.Review
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

@Adapter
class ReviewPersistenceAdapter(
    private val reviewJpaRepository: ReviewJpaRepository,
    private val reviewPersistenceMapper: ReviewPersistenceMapper,
    private val reviewImagePersistenceMapper: ReviewImagePersistenceMapper,
    private val reviewImageJpaRepository: ReviewImageJpaRepository,
) : SaveReviewPort, DeleteReviewPort, ReadReviewPort {
    @Transactional
    override fun save(review: Review): Review {
        // TODO 이대로면 리뷰 조회시마다 image 계속 제거 & 등록됨. 언젠가 개선할 것.
        val reviewEntity = reviewPersistenceMapper.toEntity(review)
        reviewImageJpaRepository.deleteAllByReviewId(reviewEntity.id)
        val savedReview = reviewJpaRepository.save(reviewEntity)
        val images = review.images.map { reviewImagePersistenceMapper.toEntity(savedReview, it) }
        val savedImages = reviewImageJpaRepository.saveAll(images)
        return reviewPersistenceMapper.toDomain(savedReview, savedImages)
    }

    override fun delete(review: Review) {
        val entity = reviewPersistenceMapper.toEntity(review)
        reviewJpaRepository.delete(entity)
    }

    override fun readById(reviewId: Long): Review {
        val review = reviewJpaRepository.getById(reviewId)
        val images = reviewImageJpaRepository.findAllByReview(review)
        return reviewPersistenceMapper.toDomain(review, images)
    }

    override fun readAllByRestaurantId(
        restaurantsId: Long,
        onlyPhotoReview: Boolean,
        page: Int,
        size: Int,
    ): SliceResult<Review> {
        val pageRequest = PageRequest.of(page, size, LATEST_SORTER)
        val reviews = reviewJpaRepository.findAllByRestaurantId(restaurantsId, onlyPhotoReview, pageRequest)
        return SliceResult.of(
            contents = reviews.content.map {
                reviewPersistenceMapper.toDomain(
                    it,
                    reviewImageJpaRepository.findAllByReview(it),
                )
            },
            currentPage = page,
            hasNext = reviews.hasNext(),
        )
    }

    override fun countByWriterId(memberId: Long): Int {
        return reviewJpaRepository.countByWriterId(memberId).toInt()
    }

    override fun countByRestaurantId(restaurantId: Long): Int {
        return reviewJpaRepository.countByRestaurantId(restaurantId).toInt()
    }

    override fun readMyReviews(memberId: Long): List<Review> {
        val reviews = reviewJpaRepository.findAllByWriterId(memberId)
        return reviews.map {
            reviewPersistenceMapper.toDomain(
                it,
                reviewImageJpaRepository.findAllByReview(it),
            )
        }
    }

    companion object {
        val LATEST_SORTER = Sort.by("createdAt").descending()
    }
}
