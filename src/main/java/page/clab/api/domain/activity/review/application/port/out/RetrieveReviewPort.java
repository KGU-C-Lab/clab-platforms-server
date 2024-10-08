package page.clab.api.domain.activity.review.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.review.domain.Review;

public interface RetrieveReviewPort {

    Review getById(Long reviewId);

    Page<Review> findAllByIsDeletedTrue(Pageable pageable);

    Page<Review> findAllByMemberId(String memberId, Pageable pageable);

    Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);

    boolean existsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup);
}
