package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MembershipFeeService {

    private final MemberLookupService memberLookupService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional
    public Long createMembershipFee(MembershipFeeRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(membershipFee);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return membershipFeeRepository.save(membershipFee).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MembershipFeeResponseDto> getMembershipFeesByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        boolean isAdminRole = memberLookupService.getCurrentMemberDetailedInfo().isAdminRole();
        Page<MembershipFee> membershipFeesPage = membershipFeeRepository.findByConditions(memberId, memberName, category, status, pageable);
        return new PagedResponseDto<>(membershipFeesPage.map(membershipFee -> {
            String applicantName = memberLookupService.getMemberBasicInfoById(membershipFee.getMemberId()).getMemberName();
            return MembershipFeeResponseDto.toDto(membershipFee, applicantName, isAdminRole);
        }));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MembershipFeeResponseDto> getDeletedMembershipFees(Pageable pageable) {
        boolean isAdminRole = memberLookupService.getCurrentMemberDetailedInfo().isAdminRole();
        Page<MembershipFee> membershipFees = membershipFeeRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee -> {
            String applicantName = memberLookupService.getMemberBasicInfoById(membershipFee.getMemberId()).getMemberName();
            return MembershipFeeResponseDto.toDto(membershipFee, applicantName, isAdminRole);
        }));
    }

    @Transactional
    public Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.update(requestDto);
        validationService.checkValid(membershipFee);
        return membershipFeeRepository.save(membershipFee).getId();
    }

    public Long deleteMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = getMembershipFeeByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.delete();
        membershipFeeRepository.save(membershipFee);
        return membershipFee.getId();
    }

    private MembershipFee getMembershipFeeByIdOrThrow(Long membershipFeeId) {
        return membershipFeeRepository.findById(membershipFeeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

}