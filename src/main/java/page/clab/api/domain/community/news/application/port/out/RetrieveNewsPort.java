package page.clab.api.domain.community.news.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.news.domain.News;

public interface RetrieveNewsPort {

    News getById(Long id);

    Page<News> findByConditions(String title, String category, Pageable pageable);
}
