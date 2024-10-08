package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.activity.review.application.dto.mapper.ReviewDtoMapper;
import page.clab.api.domain.activity.review.application.dto.request.ReviewRequestDto;
import page.clab.api.domain.activity.review.application.exception.AlreadyReviewedException;
import page.clab.api.domain.activity.review.application.port.in.RegisterReviewUseCase;
import page.clab.api.domain.activity.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewRegisterService implements RegisterReviewUseCase {

    private final RegisterReviewPort registerReviewPort;
    private final RetrieveReviewPort retrieveReviewPort;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    @Override
    public Long registerReview(ReviewRequestDto requestDto) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMemberInfo.getMemberId());
        Review review = ReviewDtoMapper.toReview(requestDto, currentMemberInfo.getMemberId(), activityGroup);
        notifyGroupLeaderOfNewReview(activityGroup, currentMemberInfo.getMemberName());
        return registerReviewPort.save(review).getId();
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, String memberId) {
        if (!activityGroup.isEnded()) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberIdAndActivityGroup(memberId, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
    }

    private void notifyGroupLeaderOfNewReview(ActivityGroup activityGroup, String memberName) {
        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (!CollectionUtils.isEmpty(groupLeaders)) {
            groupLeaders.forEach(leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(), "[" + activityGroup.getName() + "] " + memberName + "님이 리뷰를 등록하였습니다."));
        }
    }

    private boolean isExistsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup) {
        return retrieveReviewPort.existsByMemberIdAndActivityGroup(memberId, activityGroup);
    }
}
