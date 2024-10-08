package page.clab.api.domain.auth.login.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.login.application.port.out.RegisterAuthenticatorPort;
import page.clab.api.domain.auth.login.application.port.out.RemoveAuthenticatorPort;
import page.clab.api.domain.auth.login.application.port.out.RetrieveAuthenticatorPort;
import page.clab.api.domain.auth.login.domain.Authenticator;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticatorPersistenceAdapter implements
        RegisterAuthenticatorPort,
        RetrieveAuthenticatorPort,
        RemoveAuthenticatorPort {

    private final AuthenticatorRepository authenticatorRepository;
    private final AuthenticatorMapper authenticatorMapper;

    @Override
    public Authenticator save(Authenticator authenticator) {
        AuthenticatorJpaEntity jpaEntity = authenticatorMapper.toEntity(authenticator);
        AuthenticatorJpaEntity savedEntity = authenticatorRepository.save(jpaEntity);
        return authenticatorMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Authenticator> findById(String memberId) {
        return authenticatorRepository.findById(memberId).map(authenticatorMapper::toDomain);
    }

    @Override
    public Authenticator getById(String memberId) {
        return authenticatorRepository.findById(memberId)
                .map(authenticatorMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Authenticator] memberId: " + memberId + "에 해당하는 Authenticator가 존재하지 않습니다."));
    }

    @Override
    public void delete(Authenticator authenticator) {
        AuthenticatorJpaEntity jpaEntity = authenticatorMapper.toEntity(authenticator);
        authenticatorRepository.delete(jpaEntity);
    }

    @Override
    public boolean existsById(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }
}
