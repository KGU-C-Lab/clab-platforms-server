package page.clab.api.domain.community.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.news.application.dto.mapper.NewsDtoMapper;
import page.clab.api.domain.community.news.application.dto.response.NewsResponseDto;
import page.clab.api.domain.community.news.application.port.in.RetrieveNewsByConditionsUseCase;
import page.clab.api.domain.community.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.community.news.domain.News;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class NewsByConditionsRetrievalService implements RetrieveNewsByConditionsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NewsResponseDto> retrieveNews(String title, String category, Pageable pageable) {
        Page<News> newsPage = retrieveNewsPort.findByConditions(title, category, pageable);
        return new PagedResponseDto<>(newsPage.map(NewsDtoMapper::toNewsResponseDto));
    }
}
