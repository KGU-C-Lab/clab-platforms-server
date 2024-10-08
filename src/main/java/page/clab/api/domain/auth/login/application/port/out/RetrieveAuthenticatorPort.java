package page.clab.api.domain.auth.login.application.port.out;

import page.clab.api.domain.auth.login.domain.Authenticator;

import java.util.Optional;

public interface RetrieveAuthenticatorPort {

    Optional<Authenticator> findById(String memberId);

    Authenticator getById(String memberId);

    boolean existsById(String memberId);
}
