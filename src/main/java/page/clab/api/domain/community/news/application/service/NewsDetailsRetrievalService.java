package page.clab.api.domain.community.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.news.application.dto.mapper.NewsDtoMapper;
import page.clab.api.domain.community.news.application.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.community.news.application.port.in.RetrieveNewsDetailsUseCase;
import page.clab.api.domain.community.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.community.news.domain.News;

@Service
@RequiredArgsConstructor
public class NewsDetailsRetrievalService implements RetrieveNewsDetailsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;

    @Transactional(readOnly = true)
    @Override
    public NewsDetailsResponseDto retrieveNewsDetails(Long newsId) {
        News news = retrieveNewsPort.getById(newsId);
        return NewsDtoMapper.toNewsDetailsResponseDto(news);
    }
}
