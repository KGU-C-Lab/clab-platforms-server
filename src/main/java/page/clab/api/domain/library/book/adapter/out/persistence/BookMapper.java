package page.clab.api.domain.library.book.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.library.book.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookJpaEntity toEntity(Book book);

    Book toDomain(BookJpaEntity entity);
}
