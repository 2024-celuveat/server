package com.celuveat.review.application.port

import com.celuveat.common.utils.throwWhen
import com.celuveat.member.application.port.out.FindMemberPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import com.celuveat.restaurant.exception.AlreadyInterestedRestaurantException
import com.celuveat.review.application.port.`in`.ClickHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteReviewUseCase
import com.celuveat.review.application.port.`in`.UpdateReviewUseCase
import com.celuveat.review.application.port.`in`.WriteReviewUseCase
import com.celuveat.review.application.port.`in`.command.UpdateReviewCommand
import com.celuveat.review.application.port.`in`.command.WriteReviewCommand
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.DeleteReviewPort
import com.celuveat.review.application.port.out.FindHelpfulReviewPort
import com.celuveat.review.application.port.out.FindReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val findMemberPort: FindMemberPort,
    private val findRestaurantPort: FindRestaurantPort,
    private val findReviewPort: FindReviewPort,
    private val findHelpfulReviewPort: FindHelpfulReviewPort,
    private val saveHelpfulReviewPort: SaveHelpfulReviewPort,
    private val saveReviewPort: SaveReviewPort,
    private val deleteReviewPort: DeleteReviewPort,
    private val deleteHelpfulReviewPort: DeleteHelpfulReviewPort
) : WriteReviewUseCase, UpdateReviewUseCase, DeleteReviewUseCase,
    ClickHelpfulReviewUseCase, DeleteHelpfulReviewUseCase {

    override fun write(command: WriteReviewCommand): Long {
        val member = findMemberPort.getById(command.memberId)
        val restaurant = findRestaurantPort.getById(command.restaurantId)
        val review = command.toReview(member, restaurant)
        return saveReviewPort.save(review).id
    }

    override fun update(command: UpdateReviewCommand) {
        val member = findMemberPort.getById(command.memberId)
        val review = findReviewPort.getById(command.reviewId)
        review.validateWriter(member)
        review.update(command.content, command.star)
        saveReviewPort.save(review)
    }

    override fun delete(memberId: Long, reviewId: Long) {
        val member = findMemberPort.getById(memberId)
        val review = findReviewPort.getById(reviewId)
        review.validateWriter(member)
        deleteReviewPort.delete(review)
    }

    override fun clickHelpfulReview(memberId: Long, reviewId: Long) {
        throwWhen(
            findHelpfulReviewPort.existsByReviewAndMember(
                reviewId = reviewId,
                memberId = memberId
            )
        ) { throw AlreadyInterestedRestaurantException }
        val member = findMemberPort.getById(memberId)
        val review = findReviewPort.getById(reviewId)
        val helpfulReview = review.clickHelpful(member)
        saveHelpfulReviewPort.save(helpfulReview)
    }

    override fun deleteHelpfulReview(memberId: Long, reviewId: Long) {
        val helpfulReview = findHelpfulReviewPort.getByReviewAndMember(reviewId, memberId)
        helpfulReview.unClick()
        deleteHelpfulReviewPort.deleteHelpfulReview(helpfulReview)
    }
}