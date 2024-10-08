package page.clab.api.domain.auth.login.application.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.login.application.port.in.ManageAuthenticatorUseCase;
import page.clab.api.domain.auth.login.application.port.out.RegisterAuthenticatorPort;
import page.clab.api.domain.auth.login.application.port.out.RemoveAuthenticatorPort;
import page.clab.api.domain.auth.login.application.port.out.RetrieveAuthenticatorPort;
import page.clab.api.domain.auth.login.domain.Authenticator;
import page.clab.api.global.util.EncryptionUtil;

@Service
@RequiredArgsConstructor
public class AuthenticatorService implements ManageAuthenticatorUseCase {

    private final RegisterAuthenticatorPort registerAuthenticatorPort;
    private final RetrieveAuthenticatorPort retrieveAuthenticatorPort;
    private final RemoveAuthenticatorPort removeAuthenticatorPort;
    private final GoogleAuthenticator googleAuthenticator;
    private final EncryptionUtil encryptionUtil;

    @Transactional
    @Override
    public String generateSecretKey(String memberId) {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secretKey = key.getKey();
        saveAuthenticator(memberId, secretKey);
        return secretKey;
    }

    @Override
    public boolean isAuthenticatorValid(String memberId, String totp) {
        Authenticator authenticator = retrieveAuthenticatorPort.findById(memberId).orElse(null);
        return authenticator != null && validateTotp(authenticator, totp);
    }

    private boolean validateTotp(Authenticator authenticator, String totp) {
        String secretKey = encryptionUtil.decrypt(authenticator.getSecretKey());
        return googleAuthenticator.authorize(secretKey, Integer.parseInt(totp));
    }

    @Override
    public String resetAuthenticator(String memberId) {
        Authenticator authenticator = retrieveAuthenticatorPort.getById(memberId);
        removeAuthenticatorPort.delete(authenticator);
        return memberId;
    }

    private void saveAuthenticator(String memberId, String secretKey) {
        Authenticator authenticator = Authenticator.create(memberId, encryptionUtil.encrypt(secretKey));
        registerAuthenticatorPort.save(authenticator);
    }

    @Override
    public boolean isAuthenticatorExist(String memberId) {
        return retrieveAuthenticatorPort.existsById(memberId);
    }
}
