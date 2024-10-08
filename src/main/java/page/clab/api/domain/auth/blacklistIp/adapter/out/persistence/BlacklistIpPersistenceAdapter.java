package page.clab.api.domain.auth.blacklistIp.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlacklistIpPersistenceAdapter implements
        RegisterBlacklistIpPort,
        RetrieveBlacklistIpPort,
        RemoveBlacklistIpPort {

    private final BlacklistIpRepository blacklistIpRepository;
    private final BlacklistIpMapper blacklistIpMapper;

    @Override
    public BlacklistIp save(BlacklistIp blacklistIp) {
        BlacklistIpJpaEntity entity = blacklistIpMapper.toEntity(blacklistIp);
        BlacklistIpJpaEntity savedEntity = blacklistIpRepository.save(entity);
        return blacklistIpMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<BlacklistIp> findByIpAddress(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .map(blacklistIpMapper::toDomain);
    }

    @Override
    public BlacklistIp getByIpAddress(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .map(blacklistIpMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[BlacklistIp] IP: " + ipAddress + "에 해당하는 블랙리스트 IP가 존재하지 않습니다."));
    }

    @Override
    public boolean existsByIpAddress(String clientIpAddress) {
        return blacklistIpRepository.existsByIpAddress(clientIpAddress);
    }

    @Override
    public void delete(BlacklistIp blacklistIp) {
        BlacklistIpJpaEntity entity = blacklistIpMapper.toEntity(blacklistIp);
        blacklistIpRepository.delete(entity);
    }

    @Override
    public void deleteAll() {
        blacklistIpRepository.deleteAll();
    }

    @Override
    public List<BlacklistIp> findAll() {
        return blacklistIpRepository.findAll().stream()
                .map(blacklistIpMapper::toDomain)
                .toList();
    }

    @Override
    public Page<BlacklistIp> findAll(Pageable pageable) {
        return blacklistIpRepository.findAll(pageable)
                .map(blacklistIpMapper::toDomain);
    }
}
