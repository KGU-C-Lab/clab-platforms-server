package page.clab.api.domain.community.board.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.board.domain.BoardEmoji;

@Mapper(componentModel = "spring")
public interface BoardEmojiMapper {

    BoardEmojiJpaEntity toEntity(BoardEmoji boardEmoji);

    BoardEmoji toDomain(BoardEmojiJpaEntity entity);
}
