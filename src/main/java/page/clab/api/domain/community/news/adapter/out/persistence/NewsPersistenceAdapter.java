package page.clab.api.domain.community.news.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.community.news.domain.News;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class NewsPersistenceAdapter implements
        RetrieveNewsPort {

    private final NewsRepository repository;
    private final NewsMapper mapper;

    @Override
    public News getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[News] id: " + id + "에 해당하는 뉴스가 존재하지 않습니다."));
    }

    @Override
    public Page<News> findByConditions(String title, String category, Pageable pageable) {
        return repository.findByConditions(title, category, pageable)
                .map(mapper::toDomain);
    }
}
