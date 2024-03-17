package page.clab.api.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String id;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.member.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 9, max = 11, message = "{size.member.contact}")
    private String contact;

    @Column(nullable = false)
    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.department}")
    private String department;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.member.grade}")
    @Max(value = 4, message = "{max.member.grade}")
    private Long grade;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.address}")
    private String address;

    @Column(nullable = false)
    private String interests;

    @URL(message = "{url.member.githubUrl}")
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginTime;

    private LocalDateTime loanSuspensionDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().getKey()));
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Member of(MemberRequestDto memberRequestDto) {
        Member member = ModelMapperUtil.getModelMapper().map(memberRequestDto, Member.class);
        member.setRole(Role.USER);
        return member;
    }

    public static Member of(Application application) {
        Member member = ModelMapperUtil.getModelMapper().map(application, Member.class);
        member.setId(application.getStudentId());
        member.setPassword(null);
        member.setStudentStatus(StudentStatus.CURRENT);
        member.setRole(Role.USER);
        return member;
    }

    public void update(MemberUpdateRequestDto memberUpdateRequestDto, PasswordEncoder passwordEncoder) {
        Optional.ofNullable(memberUpdateRequestDto.getPassword())
                .ifPresent(password -> setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(memberUpdateRequestDto.getContact()).ifPresent(this::setContact);
        Optional.ofNullable(memberUpdateRequestDto.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(memberUpdateRequestDto.getGrade()).ifPresent(this::setGrade);
        Optional.ofNullable(memberUpdateRequestDto.getBirth()).ifPresent(this::setBirth);
        Optional.ofNullable(memberUpdateRequestDto.getAddress()).ifPresent(this::setAddress);
        Optional.ofNullable(memberUpdateRequestDto.getInterests()).ifPresent(this::setInterests);
        Optional.ofNullable(memberUpdateRequestDto.getGithubUrl()).ifPresent(this::setGithubUrl);
        Optional.ofNullable(memberUpdateRequestDto.getStudentStatus()).ifPresent(this::setStudentStatus);
        Optional.ofNullable(memberUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

    public void checkLoanSuspension() {
        if (loanSuspensionDate != null && LocalDateTime.now().isBefore(loanSuspensionDate)) {
            throw new LoanSuspensionException("대출 정지 중입니다. 대출 정지일까지는 책을 대출할 수 없습니다.");
        }
    }

    public void handleOverdueAndSuspension(long overdueDays) {
        if (overdueDays > 0) {
            LocalDateTime currentDate = LocalDateTime.now();
            if (loanSuspensionDate == null || loanSuspensionDate.isBefore(currentDate)) {
                loanSuspensionDate = LocalDateTime.now().plusDays(overdueDays * 7);;
            } else {
                loanSuspensionDate = loanSuspensionDate.plusDays(overdueDays * 7);
            }
        }
    }
}
