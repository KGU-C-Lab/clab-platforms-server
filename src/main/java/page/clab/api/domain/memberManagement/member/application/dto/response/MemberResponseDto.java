package page.clab.api.domain.memberManagement.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.domain.memberManagement.member.domain.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDto {

    private String id;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String githubUrl;
    private StudentStatus studentStatus;
    private String imageUrl;
    private Role role;
    private LocalDateTime lastLoginTime;
    private LocalDateTime loanSuspensionDate;
    private Boolean isOtpEnabled;
    private LocalDateTime createdAt;
}
