package page.clab.api.domain.activity.activitygroup.dto.mapper.request;

import page.clab.api.domain.activity.activitygroup.domain.Absent;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupReport;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.ApplyForm;
import page.clab.api.domain.activity.activitygroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.util.List;

public class ActivityGroupRequestDtoMapper {

    public static Absent toAbsent(AbsentRequestDto requestDto, Member absentee, ActivityGroup activityGroup) {
        return Absent.builder()
                .memberId(absentee.getId())
                .activityGroup(activityGroup)
                .absentDate(requestDto.getAbsentDate())
                .reason(requestDto.getReason())
                .build();
    }

    public static ActivityGroupBoard toActivityGroupBoard(ActivityGroupBoardRequestDto requestDto, Member member, ActivityGroup activityGroup, ActivityGroupBoard parentBoard, List<UploadedFile> uploadedFiles) {
        return ActivityGroupBoard.builder()
                .activityGroup(activityGroup)
                .memberId(member.getId())
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .parent(parentBoard)
                .uploadedFiles(uploadedFiles)
                .dueDateTime(requestDto.getDueDateTime())
                .isDeleted(false)
                .build();
    }

    public static ActivityGroupReport toActivityGroupReport(ActivityGroupReportRequestDto requestDto, ActivityGroup activityGroup) {
        return ActivityGroupReport.builder()
                .turn(requestDto.getTurn())
                .activityGroup(activityGroup)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isDeleted(false)
                .build();
    }

    public static ActivityGroup toActivityGroup(ActivityGroupRequestDto requestDto) {
        return ActivityGroup.builder()
                .category(requestDto.getCategory())
                .subject(requestDto.getSubject())
                .name(requestDto.getName())
                .content(requestDto.getContent())
                .status(ActivityGroupStatus.WAITING)
                .progress(0L)
                .imageUrl(requestDto.getImageUrl())
                .curriculum(requestDto.getCurriculum())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .techStack(requestDto.getTechStack())
                .githubUrl(requestDto.getGithubUrl())
                .isDeleted(false)
                .build();
    }

    public static ApplyForm toApplyForm(ApplyFormRequestDto requestDto, ActivityGroup activityGroup, Member member) {
        return ApplyForm.builder()
                .activityGroup(activityGroup)
                .memberId(member.getId())
                .applyReason(requestDto.getApplyReason())
                .build();
    }
}
