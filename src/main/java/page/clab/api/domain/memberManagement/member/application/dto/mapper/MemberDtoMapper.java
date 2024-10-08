package page.clab.api.domain.memberManagement.member.application.dto.mapper;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberResponseDto;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;
import page.clab.api.domain.memberManagement.member.application.dto.response.MyProfileResponseDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberEmailInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberReviewInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.common.domain.Contact;

public class MemberDtoMapper {

    public static Member toMember(MemberRequestDto requestDto) {
        return Member.builder()
                .id(requestDto.getId())
                .password(requestDto.getPassword())
                .name(requestDto.getName())
                .contact(Contact.of(requestDto.getContact()).getValue())
                .email(requestDto.getEmail())
                .department(requestDto.getDepartment())
                .grade(requestDto.getGrade())
                .birth(requestDto.getBirth())
                .address(requestDto.getAddress())
                .interests(requestDto.getInterests())
                .githubUrl(requestDto.getGithubUrl())
                .studentStatus(requestDto.getStudentStatus())
                .imageUrl(requestDto.getImageUrl())
                .role(Role.USER)
                .isOtpEnabled(false)
                .isDeleted(false)
                .build();
    }

    public static MemberBirthdayResponseDto toMemberBirthdayResponseDto(Member member) {
        return MemberBirthdayResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .birth(member.getBirth())
                .imageUrl(member.getImageUrl())
                .build();
    }

    public static MemberResponseDto toMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .contact(member.getContact())
                .email(member.getEmail())
                .department(member.getDepartment())
                .grade(member.getGrade())
                .birth(member.getBirth())
                .address(member.getAddress())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .studentStatus(member.getStudentStatus())
                .imageUrl(member.getImageUrl())
                .role(member.getRole())
                .lastLoginTime(member.getLastLoginTime())
                .loanSuspensionDate(member.getLoanSuspensionDate())
                .isOtpEnabled(member.getIsOtpEnabled())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberRoleInfoResponseDto toMemberRoleInfoResponseDto(Member member) {
        return MemberRoleInfoResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }

    public static MyProfileResponseDto toMyProfileResponseDto(Member member) {
        return MyProfileResponseDto.builder()
                .name(member.getName())
                .id(member.getId())
                .interests(member.getInterests())
                .contact(member.getContact())
                .email(member.getEmail())
                .address(member.getAddress())
                .githubUrl(member.getGithubUrl())
                .studentStatus(member.getStudentStatus())
                .imageUrl(member.getImageUrl())
                .roleLevel(member.getRole().toRoleLevel())
                .isOtpEnabled(member.getIsOtpEnabled())
                .createdAt(member.getCreatedAt())
                .build();
    }


    public static MemberReviewInfoDto toMemberReviewInfoDto(Member member) {
        return MemberReviewInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .department(member.getDepartment())
                .build();
    }

    public static MemberPositionInfoDto toMemberPositionInfoDto(Member member) {
        return MemberPositionInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .build();
    }

    public static MemberLoginInfoDto toMemberLoginInfoDto(Member member) {
        return MemberLoginInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(member.getRole())
                .isOtpEnabled(member.getIsOtpEnabled())
                .build();
    }

    public static MemberEmailInfoDto toMemberEmailInfoDto(Member member) {
        return MemberEmailInfoDto.builder()
                .memberName(member.getName())
                .email(member.getEmail())
                .build();
    }

    public static MemberDetailedInfoDto toMemberDetailedInfoDto(Member member) {
        return MemberDetailedInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .roleLevel(member.getRole().toRoleLevel())
                .imageUrl(member.getImageUrl())
                .isGraduated(member.isGraduated())
                .build();
    }

    public static MemberBorrowerInfoDto toMemberBorrowerInfoDto(Member member) {
        return MemberBorrowerInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .loanSuspensionDate(member.getLoanSuspensionDate())
                .build();
    }

    public static MemberBasicInfoDto toMemberBasicInfoDto(Member member) {
        return MemberBasicInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .build();
    }
}
