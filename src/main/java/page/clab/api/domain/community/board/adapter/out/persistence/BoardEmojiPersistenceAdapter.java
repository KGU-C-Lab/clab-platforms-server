package page.clab.api.domain.community.board.adapter.out.persistence;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.domain.BoardEmoji;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardEmojiPersistenceAdapter implements
        RetrieveBoardEmojiPort,
        RegisterBoardEmojiPort {

    private final BoardEmojiRepository boardEmojiRepository;
    private final BoardEmojiMapper boardEmojiMapper;

    @Override
    public List<Tuple> findEmojiClickCountsByBoardId(Long boardId, String memberId) {
        return boardEmojiRepository.findEmojiClickCountsByBoardId(boardId, memberId);
    }

    @Override
    public Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmoji(Long boardId, String memberId, String emoji) {
        return boardEmojiRepository.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(boardEmojiMapper::toDomain);
    }

    @Override
    public BoardEmoji save(BoardEmoji boardEmoji) {
        BoardEmojiJpaEntity entity = boardEmojiMapper.toEntity(boardEmoji);
        BoardEmojiJpaEntity savedEntity = boardEmojiRepository.save(entity);
        return boardEmojiMapper.toDomain(savedEntity);
    }
}
