package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
        CheckMemberExistencePort,
        RegisterMemberPort,
        UpdateMemberPort,
        RetrieveMemberPort {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public boolean existsById(String id) {
        return memberRepository.existsById(id);
    }

    @Override
    public boolean existsByContact(String contact) {
        return memberRepository.existsByContact(contact);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Member save(Member member) {
        MemberJpaEntity jpaEntity = memberMapper.toEntity(member);
        MemberJpaEntity savedEntity = memberRepository.save(jpaEntity);
        return memberMapper.toDomain(savedEntity);
    }

    @Override
    public Member update(Member member) {
        MemberJpaEntity jpaEntity = memberMapper.toEntity(member);
        MemberJpaEntity updatedEntity = memberRepository.save(jpaEntity);
        return memberMapper.toDomain(updatedEntity);
    }

    @Override
    public Page<Member> findByConditions(String id, String name, Pageable pageable) {
        Page<MemberJpaEntity> jpaEntities = memberRepository.findByConditions(id, name, pageable);
        return jpaEntities.map(memberMapper::toDomain);
    }

    @Override
    public Member getFirstByRole(Role role) {
        MemberJpaEntity jpaEntity = memberRepository.findFirstByRole(role)
                .orElseThrow(() -> new NotFoundException("[Member] role: " + role + "에 해당하는 회원이 존재하지 않습니다."));
        return memberMapper.toDomain(jpaEntity);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId).map(memberMapper::toDomain);
    }

    @Override
    public Member getById(String memberId) {
        MemberJpaEntity jpaEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다."));
        return memberMapper.toDomain(jpaEntity);
    }

    @Override
    public List<Member> findAll() {
        List<MemberJpaEntity> jpaEntities = memberRepository.findAll();
        return jpaEntities.stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    public Page<Member> findMemberRoleInfoByConditions(String memberId, String memberName, Role role, Pageable pageable) {
        Page<MemberJpaEntity> jpaEntities = memberRepository.findMemberRoleInfoByConditions(memberId, memberName, role, pageable);
        return jpaEntities.map(memberMapper::toDomain);
    }

    @Override
    public Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        Page<MemberJpaEntity> jpaEntities = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return jpaEntities.map(memberMapper::toDomain);
    }

    @Override
    public Member getByEmail(String email) {
        MemberJpaEntity jpaEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("[Member] email: " + email + "을 사용하는 회원이 존재하지 않습니다."));
        return memberMapper.toDomain(jpaEntity);
    }

    @Override
    public Page<Member> findBirthdaysThisMonth(int month, Pageable pageable) {
        Page<MemberJpaEntity> jpaEntities = memberRepository.findBirthdaysThisMonth(month, pageable);
        return jpaEntities.map(memberMapper::toDomain);
    }
}
