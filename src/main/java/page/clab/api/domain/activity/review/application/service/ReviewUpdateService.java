package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.review.application.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.activity.review.application.port.in.UpdateReviewUseCase;
import page.clab.api.domain.activity.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewUpdateService implements UpdateReviewUseCase {

    private final RetrieveReviewPort retrieveReviewPort;
    private final RegisterReviewPort registerReviewPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMember = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Review review = retrieveReviewPort.getById(reviewId);
        review.validateAccessPermission(currentMember);
        review.update(requestDto);
        return registerReviewPort.save(review).getId();
    }
}
