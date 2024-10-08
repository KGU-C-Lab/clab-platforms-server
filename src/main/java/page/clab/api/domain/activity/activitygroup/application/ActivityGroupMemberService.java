package page.clab.api.domain.activity.activitygroup.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupDetailsRepository;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activity.activitygroup.dao.ApplyFormRepository;
import page.clab.api.domain.activity.activitygroup.dao.GroupMemberRepository;
import page.clab.api.domain.activity.activitygroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.ApplyForm;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupSchedule;
import page.clab.api.domain.activity.activitygroup.dto.mapper.param.ActivityGroupParamDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.mapper.request.ActivityGroupRequestDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.mapper.response.ActivityGroupResponseDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.param.ActivityGroupDetails;
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupDetailResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.LeaderInfo;
import page.clab.api.domain.activity.activitygroup.exception.AlreadyAppliedException;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.util.PaginationUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupMemberService {

    private final ActivityGroupRepository activityGroupRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ActivityGroupBoardRepository activityGroupBoardRepository;
    private final ApplyFormRepository applyFormRepository;
    private final ActivityGroupDetailsRepository activityGroupDetailsRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional(readOnly = true)
    public ActivityGroupDetailResponseDto getActivityGroup(Long activityGroupId) {
        ActivityGroupDetails details = activityGroupDetailsRepository.fetchActivityGroupDetails(activityGroupId);
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();

        boolean isOwner = details.getGroupMembers().stream()
                .anyMatch(groupMember -> groupMember.isOwnerAndLeader(currentMember));

        List<GroupMemberResponseDto> groupMemberResponseDtos = details.getGroupMembers().stream()
                .map(groupMember -> ActivityGroupResponseDtoMapper.toGroupMemberResponseDto(externalRetrieveMemberUseCase.getById(groupMember.getMemberId()), groupMember))
                .toList();

        List<ActivityGroupBoardResponseDto> activityGroupBoardResponseDtos =
                details.getActivityGroupBoards().stream()
                        .map(board -> {
                            MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
                            return ActivityGroupResponseDtoMapper.toActivityGroupBoardResponseDto(board, memberBasicInfoDto);
                        })
                        .toList();

        return ActivityGroupResponseDtoMapper.toActivityGroupDetailResponseDto(details.getActivityGroup(), activityGroupBoardResponseDtos, groupMemberResponseDtos, isOwner);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupStatusResponseDto> getMyActivityGroups(ActivityGroupStatus status, Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        List<GroupMember> groupMembers = getGroupMemberByMemberId(currentMemberId);

        List<ActivityGroup> activityGroups = groupMembers.stream()
                .filter(GroupMember::isAccepted)
                .map(GroupMember::getActivityGroup)
                .filter(activityGroup -> status == null || activityGroup.isSameStatus(status))
                .distinct()
                .toList();

        List<ActivityGroupStatusResponseDto> activityGroupDtos = activityGroups.stream()
                .map(this::getActivityGroupStatusResponseDto)
                .toList();

        List<ActivityGroupStatusResponseDto> paginatedActivityGroupDtos = PaginationUtils.applySortingAndSlicing(activityGroupDtos, pageable);

        return new PagedResponseDto<>(paginatedActivityGroupDtos, activityGroups.size(), pageable);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupStatusResponseDto> getActivityGroupsByStatus(ActivityGroupStatus status, Pageable pageable) {
        Page<ActivityGroup> activityGroups = activityGroupRepository.findActivityGroupsByStatus(status, pageable);
        return new PagedResponseDto<>(activityGroups.map(this::getActivityGroupStatusResponseDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByCategory(ActivityGroupCategory category, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByCategory(category, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDtoMapper::toActivityGroupResponseDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<GroupScheduleDto> getGroupSchedules(Long activityGroupId, Pageable pageable) {
        Page<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupSchedules.map(ActivityGroupParamDtoMapper::toGroupScheduleDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<GroupMemberResponseDto> getActivityGroupMembers(Long activityGroupId, Pageable pageable) {
        Page<GroupMember> groupMembers = getGroupMemberByActivityGroupIdAndStatus(activityGroupId, GroupMemberStatus.ACCEPTED, pageable);
        return new PagedResponseDto<>(groupMembers.map(groupMember -> {
            Member member = externalRetrieveMemberUseCase.getById(groupMember.getMemberId());
            return ActivityGroupResponseDtoMapper.toGroupMemberResponseDto(member, groupMember);
        }));
    }

    @Transactional
    public Long applyActivityGroup(Long activityGroupId, ApplyFormRequestDto formRequestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.validateForApplication();
        if (isGroupMember(activityGroup, currentMember.getId())) {
            throw new AlreadyAppliedException("해당 활동에 신청한 내역이 존재합니다.");
        }

        ApplyForm form = ActivityGroupRequestDtoMapper.toApplyForm(formRequestDto, activityGroup, currentMember);
        applyFormRepository.save(form);

        GroupMember groupMember = GroupMember.create(currentMember.getId(), activityGroup, ActivityGroupRole.NONE, GroupMemberStatus.WAITING);
        groupMemberRepository.save(groupMember);

        List<GroupMember> groupLeaders = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (!CollectionUtils.isEmpty(groupLeaders)) {
            groupLeaders.forEach(leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(), "[" + activityGroup.getName() + "] " + currentMember.getName() + "님이 활동 참가 신청을 하였습니다."));
        }
        return activityGroup.getId();
    }

    public PagedResponseDto<ActivityGroupStatusResponseDto> getAppliedActivityGroups(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        List<GroupMember> groupMembers = getGroupMemberByMemberId(currentMemberId);

        List<Long> activityGroupIds = groupMembers.stream()
                .map(GroupMember::getActivityGroup)
                .map(ActivityGroup::getId)
                .distinct()
                .toList();

        Map<Long, GroupMember> activityGroupOwners = findActivityGroupOwners(activityGroupIds);

        List<ActivityGroupStatusResponseDto> activityGroups = groupMembers.stream()
                .filter(groupMember -> !isActivityGroupOwner(groupMember, activityGroupOwners))
                .map(GroupMember::getActivityGroup)
                .filter(ActivityGroup::isProgressing)
                .distinct()
                .map(this::getActivityGroupStatusResponseDto)
                .toList();

        List<ActivityGroupStatusResponseDto> paginatedActivityGroups = PaginationUtils.applySortingAndSlicing(activityGroups, pageable);

        return new PagedResponseDto<>(paginatedActivityGroups, activityGroups.size(), pageable);
    }

    private ActivityGroupStatusResponseDto getActivityGroupStatusResponseDto(ActivityGroup activityGroup) {
        Long activityGroupId = activityGroup.getId();

        Long participantCount = groupMemberRepository.countAcceptedMembersByActivityGroupId(activityGroupId);
        List<LeaderInfo> leaderMembers = groupMemberRepository.findLeaderByActivityGroupId(activityGroupId)
                .stream()
                .map(leader -> {
                    Member member = externalRetrieveMemberUseCase.getById(leader.getMemberId());
                    LocalDateTime createdAt = leader.getCreatedAt();
                    return ActivityGroupResponseDtoMapper.toLeaderInfo(member, createdAt);
                })
                // LEADER 직책을 가진 사람 중 가장 먼저 활동에 참여한 사람 순으로 정렬
                .sorted(Comparator.comparing(LeaderInfo::getCreatedAt))
                .toList();

        Long weeklyActivityCount = activityGroupBoardRepository.countByActivityGroupIdAndCategory(activityGroupId, ActivityGroupBoardCategory.WEEKLY_ACTIVITY);

        return ActivityGroupResponseDtoMapper.toActivityGroupStatusResponseDto(activityGroup, leaderMembers, participantCount, weeklyActivityCount);
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    public Map<Long, GroupMember> findActivityGroupOwners(List<Long> activityGroupIds) {
        return groupMemberRepository.findFirstByActivityGroupIdIn(activityGroupIds).stream()
                .collect(Collectors.toMap(
                        groupMember -> groupMember.getActivityGroup().getId(),
                        Function.identity()
                ));
    }

    public Optional<GroupMember> getGroupMemberByActivityGroupAndMember(ActivityGroup activityGroup, String memberId) {
        return groupMemberRepository.findByActivityGroupAndMemberId(activityGroup, memberId);
    }

    public GroupMember getGroupMemberByActivityGroupAndMemberOrThrow(ActivityGroup activityGroup, String memberId) {
        return groupMemberRepository.findByActivityGroupAndMemberId(activityGroup, memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 활동에 참여하지 않았습니다."));
    }

    private Page<ActivityGroup> getActivityGroupByCategory(ActivityGroupCategory category, Pageable pageable) {
        return activityGroupRepository.findAllByCategory(category, pageable);
    }

    private Page<GroupSchedule> getGroupScheduleByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupScheduleRepository.findAllByActivityGroupId(activityGroupId, pageable);
    }

    public List<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId) {
        return groupMemberRepository.findAllByActivityGroupIdOrderByMemberIdAsc(activityGroupId);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupId(activityGroupId, pageable);
    }

    public List<GroupMember> getGroupMemberByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status) {
        return groupMemberRepository.findAllByActivityGroupIdAndStatus(activityGroupId, status);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupIdAndStatus(activityGroupId, status, pageable);
    }

    public List<GroupMember> getGroupMemberByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role) {
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role);
    }

    public List<GroupMember> getGroupMemberByMemberId(String memberId) {
        return groupMemberRepository.findAllByMemberId(memberId);
    }

    public boolean isGroupMember(ActivityGroup activityGroup, String memberId) {
        return groupMemberRepository.existsByActivityGroupAndMemberIdAndStatus(activityGroup, memberId, GroupMemberStatus.ACCEPTED);
    }

    private boolean isActivityGroupOwner(GroupMember groupMember, Map<Long, GroupMember> activityGroupOwners) {
        ActivityGroup activityGroup = groupMember.getActivityGroup();
        return activityGroupOwners.get(activityGroup.getId()).equals(groupMember);
    }

    public GroupMember save(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    public void deleteAll(List<GroupMember> groupMemberList) {
        groupMemberRepository.deleteAll(groupMemberList);
    }
}
