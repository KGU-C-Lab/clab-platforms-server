package page.clab.api.domain.community.board.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.board.domain.Board;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardJpaEntity toEntity(Board board);

    Board toDomain(BoardJpaEntity entity);
}
