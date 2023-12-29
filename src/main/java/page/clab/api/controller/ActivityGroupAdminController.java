package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ActivityGroupAdminService;
import page.clab.api.type.dto.ActivityGroupRequestDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupMemberResponseDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.type.etc.GroupMemberStatus;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/activity-group/admin")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupAdmin", description = "활동 그룹 관리 관련 API")
@Slf4j
public class ActivityGroupAdminController {

    private final ActivityGroupAdminService activityGroupAdminService;

    @Operation(summary = "[U] 활동 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createActivityGroup(
            @Valid @RequestBody ActivityGroupRequestDto activityGroupRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = activityGroupAdminService.createActivityGroup(activityGroupRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 활동 상태별 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getActivityGroupsByStatus (
            @RequestParam ActivityGroupStatus activityGroupStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupResponseDto> activityGroupList = activityGroupAdminService.getActivityGroupsByStatus(activityGroupStatus, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityGroupList);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{activityGroupId}")
    public ResponseModel updateActivityGroup(
            @PathVariable Long activityGroupId,
            @Valid @RequestBody ActivityGroupRequestDto activityGroupRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = activityGroupAdminService.updateActivityGroup(activityGroupId, activityGroupRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 활동 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("manage/{activityGroupId}")
    public ResponseModel manageActivityGroupStatus(
            @PathVariable Long activityGroupId,
            @RequestParam ActivityGroupStatus activityGroupStatus
    ) {
        Long id = activityGroupAdminService.manageActivityGroup(activityGroupId, activityGroupStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 활동 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{activityGroupId}")
    public ResponseModel deleteActivityGroup(
            @PathVariable Long activityGroupId
    ) {
        Long id = activityGroupAdminService.deleteActivityGroup(activityGroupId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 프로젝트 진행도 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "진행도는 0~100 사이의 값으로 입력해야 함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/progress/{activityGroupId}")
    public ResponseModel updateProjectProgress(
            @PathVariable Long activityGroupId,
            @RequestParam Long progress
    ) throws PermissionDeniedException {
        Long id = activityGroupAdminService.updateProjectProgress(activityGroupId, progress);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 커리큘럼 및 일정 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/schedule")
    public ResponseModel addSchedule(
            @RequestParam Long activityGroupId,
            @Valid @RequestBody List<GroupScheduleDto> groupScheduleDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = activityGroupAdminService.addSchedule(activityGroupId, groupScheduleDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 신청 멤버 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/apply-members")
    public ResponseModel getApplyGroupMemberList(
            @RequestParam Long activityGroupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<GroupMemberResponseDto> applyMemberList = activityGroupAdminService.getApplyGroupMemberList(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(applyMemberList);
        return responseModel;
    }

    @Operation(summary = "[U] 신청 멤버 상태 변경", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/accept")
    public ResponseModel acceptGroupMember(
            @RequestParam String memberId,
            @RequestParam GroupMemberStatus status
    ) throws PermissionDeniedException {
        String id = activityGroupAdminService.manageGroupMemberStatus(memberId, status);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
