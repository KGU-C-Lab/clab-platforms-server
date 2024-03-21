package page.clab.api.domain.news.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_article_url", columnList = "articleUrl")})
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.news.title}")
    private String title;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.news.category}")
    private String category;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.news.content}")
    private String content;

    @Column(nullable = false)
    @URL(message = "{url.news.articleUrl}")
    private String articleUrl;

    @Column(nullable = false)
    private String source;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_files")
    private List<UploadedFile> uploadedFiles = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate date;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static News create(NewsRequestDto newsRequestDto) {
        return ModelMapperUtil.getModelMapper().map(newsRequestDto, News.class);
    }

    public void update(NewsUpdateRequestDto newsUpdateRequestDto) {
        Optional.ofNullable(newsUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(newsUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(newsUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(newsUpdateRequestDto.getArticleUrl()).ifPresent(this::setArticleUrl);
        Optional.ofNullable(newsUpdateRequestDto.getSource()).ifPresent(this::setSource);
        Optional.ofNullable(newsUpdateRequestDto.getDate()).ifPresent(this::setDate);
    }

}