package page.clab.api.domain.activity.activitygroup.application;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.activitygroup.dao.AbsentRepository;
import page.clab.api.domain.activity.activitygroup.dao.AttendanceRepository;
import page.clab.api.domain.activity.activitygroup.dao.RedisQRKeyRepository;
import page.clab.api.domain.activity.activitygroup.domain.Absent;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.Attendance;
import page.clab.api.domain.activity.activitygroup.domain.RedisQRKey;
import page.clab.api.domain.activity.activitygroup.dto.mapper.request.ActivityGroupRequestDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.mapper.response.ActivityGroupResponseDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.AttendanceRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AbsentResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AttendanceResponseDto;
import page.clab.api.domain.activity.activitygroup.exception.DuplicateAbsentExcuseException;
import page.clab.api.domain.activity.activitygroup.exception.DuplicateAttendanceException;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.QRCodeUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final ActivityGroupAdminService activityGroupAdminService;
    private final AttendanceRepository attendanceRepository;
    private final RedisQRKeyRepository redisQRKeyRepository;
    private final AbsentRepository absentRepository;
    private final GoogleAuthenticator googleAuthenticator;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final FileService fileService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotNull
    private static String buildPathForQRCode(ActivityGroup activityGroup) {
        return "attendance" + File.separator
                + activityGroup.getCategory().toString() + File.separator
                + activityGroup.getId().toString();
    }

    @NotNull
    private static String generateQRCodeURL(Long activityGroupId, String secretKey) {
        return "clab.page/attendance?activityGroupId=" + activityGroupId.toString() + "&secretKey=" + secretKey;
    }

    @Transactional
    public String generateAttendanceQRCode(Long activityGroupId) throws IOException, WriterException, PermissionDeniedException, IllegalAccessException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = validateAttendanceQRCodeGeneration(activityGroupId, currentMember);

        String nowDateTime = LocalDateTime.now().format(dateTimeFormatter);
        String secretKey = googleAuthenticator.createCredentials().getKey();
        RedisQRKey redisQRKey = RedisQRKey.create(secretKey);
        redisQRKeyRepository.save(redisQRKey);

        String url = generateQRCodeURL(activityGroupId, secretKey);
        Attendance attendance = Attendance.create(currentMember.getId(), activityGroup, LocalDate.parse(nowDateTime.split(" ")[0], dateFormatter));
        attendanceRepository.save(attendance);

        byte[] QRCodeImage = QRCodeUtil.encodeQRCode(url);
        String path = buildPathForQRCode(activityGroup);
        return fileService.saveQRCodeImage(QRCodeImage, path, 1, nowDateTime);
    }

    @Transactional
    public Long checkMemberAttendance(AttendanceRequestDto requestDto) throws IllegalAccessException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = validateMemberForAttendance(currentMember, requestDto.getActivityGroupId());
        validateQRCodeData(requestDto.getQRCodeSecretKey());
        Attendance attendance = Attendance.create(currentMember.getId(), activityGroup, LocalDate.now());
        return attendanceRepository.save(attendance).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AttendanceResponseDto> getMyAttendances(Long activityGroupId, Pageable pageable) throws IllegalAccessException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = validateGroupAndMemberForAttendance(activityGroupId, currentMember);
        Page<Attendance> attendances = getAttendanceByMemberId(activityGroup, currentMember.getId(), pageable);
        return new PagedResponseDto<>(attendances.map(ActivityGroupResponseDtoMapper::toAttendanceResponseDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AttendanceResponseDto> getGroupAttendances(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupWithValidPermissions(activityGroupId, currentMember);
        Page<Attendance> attendances = getAttendanceByActivityGroup(activityGroup, pageable);
        return new PagedResponseDto<>(attendances.map(ActivityGroupResponseDtoMapper::toAttendanceResponseDto));
    }

    @Transactional
    public Long writeAbsentExcuse(AbsentRequestDto requestDto) throws IllegalAccessException, DuplicateAbsentExcuseException {
        Member absentee = externalRetrieveMemberUseCase.getById(requestDto.getAbsenteeId());
        ActivityGroup activityGroup = getValidActivityGroup(requestDto.getActivityGroupId());
        validateAbsentExcuseConditions(absentee, activityGroup, requestDto.getAbsentDate());
        Absent absent = ActivityGroupRequestDtoMapper.toAbsent(requestDto, absentee, activityGroup);
        return absentRepository.save(absent).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AbsentResponseDto> getActivityGroupAbsentExcuses(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupWithPermissionCheck(activityGroupId, currentMember);
        Page<Absent> absents = absentRepository.findAllByActivityGroup(activityGroup, pageable);
        return new PagedResponseDto<>(absents.map(absent -> {
            Member member = externalRetrieveMemberUseCase.getById(absent.getMemberId());
            return ActivityGroupResponseDtoMapper.toAbsentResponseDto(absent, member);
        }));
    }

    private Page<Attendance> getAttendanceByMemberId(ActivityGroup activityGroup, String memberId, Pageable pageable) {
        return attendanceRepository.findAllByMemberIdAndActivityGroup(memberId, activityGroup, pageable);
    }

    private Page<Attendance> getAttendanceByActivityGroup(ActivityGroup activityGroup, Pageable pageable) {
        return attendanceRepository.findAllByActivityGroup(activityGroup, pageable);
    }

    public boolean hasAttendanceHistory(ActivityGroup activityGroup, String memberId, LocalDate activityDate) {
        Attendance attendanceHistory = attendanceRepository.findByActivityGroupAndMemberIdAndActivityDate(activityGroup, memberId, activityDate);
        return attendanceHistory != null;
    }

    public boolean isActivityExistedAt(ActivityGroup activityGroup, LocalDate date) {
        return attendanceRepository.existsByActivityGroupAndActivityDate(activityGroup, date);
    }

    public boolean hasAbsentExcuseHistory(ActivityGroup activityGroup, Member absentee, LocalDate absentDate) {
        Absent absentHistory = absentRepository.findByActivityGroupAndMemberIdAndAbsentDate(activityGroup, absentee.getId(), absentDate);
        return absentHistory != null;
    }

    @NotNull
    private ActivityGroup validateAttendanceQRCodeGeneration(Long activityGroupId, Member member) throws PermissionDeniedException, IllegalAccessException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);

        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 LEADER만 출석체크 QR을 생성할 수 있습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 출석 QR 코드를 생성할 수 없습니다.");
        }
        return activityGroup;
    }

    private ActivityGroup validateMemberForAttendance(Member member, Long activityGroupId) throws IllegalAccessException, DuplicateAttendanceException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.MEMBER, activityGroupId)) {
            throw new IllegalAccessException("해당 그룹의 멤버가 아닙니다. 출석체크 인증을 진행할 수 없습니다.");
        }
        LocalDate today = LocalDate.now();
        if (hasAttendanceHistory(activityGroup, member.getId(), today)) {
            throw new DuplicateAttendanceException("이미 오늘의 출석 기록이 있습니다.");
        }
        return activityGroup;
    }

    private void validateQRCodeData(String QRCodeData) throws InvalidInformationException, NotFoundException {
        if (QRCodeData == null || !redisQRKeyRepository.existsByQRCodeKey(QRCodeData)) {
            throw new NotFoundException("QRCode 정보가 없거나 잘못 되었습니다.");
        }
    }

    @NotNull
    private ActivityGroup validateGroupAndMemberForAttendance(Long activityGroupId, Member member) throws IllegalAccessException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.MEMBER, activityGroupId)) {
            throw new IllegalAccessException("해당 그룹의 멤버가 아닙니다. 출석체크 인증을 진행할 수 없습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 출석 정보를 불러올 수 없습니다.");
        }
        return activityGroup;
    }

    private ActivityGroup getActivityGroupWithValidPermissions(Long activityGroupId, Member member) throws PermissionDeniedException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!member.isAdminRole() || !activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("그룹의 출석기록을 조회할 권한이 없습니다.");
        }
        return activityGroup;
    }

    private ActivityGroup getValidActivityGroup(Long activityGroupId) throws IllegalAccessException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 불참 사유서를 등록할 수 없습니다.");
        }
        return activityGroup;
    }

    private void validateAbsentExcuseConditions(Member absentee, ActivityGroup activityGroup, LocalDate absentDate) throws IllegalAccessException, DuplicateAbsentExcuseException {
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(absentee, ActivityGroupRole.MEMBER, activityGroup.getId())) {
            throw new IllegalAccessException("해당 그룹의 멤버가 아닙니다. 불참 사유서를 등록할 수 없습니다.");
        }
        if (!isActivityExistedAt(activityGroup, absentDate)) {
            throw new NotFoundException("해당 날짜에 진행한 그룹 활동이 없습니다. 불참 사유서를 등록할 수 없습니다.");
        }
        if (hasAttendanceHistory(activityGroup, absentee.getId(), absentDate)) {
            throw new IllegalAccessException("해당 요일에 출석한 기록이 있습니다. 불참 사유서를 등록할 수 없습니다.");
        }
        if (hasAbsentExcuseHistory(activityGroup, absentee, absentDate)) {
            throw new DuplicateAbsentExcuseException("이미 해당 결석에 대해 불참 사유서가 등록되어 있습니다.");
        }
    }

    private ActivityGroup getActivityGroupWithPermissionCheck(Long activityGroupId, Member member) throws PermissionDeniedException {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!member.isAdminRole() && !activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 불참사유서를 열람할 권한이 부족합니다.");
        }
        return activityGroup;
    }
}
