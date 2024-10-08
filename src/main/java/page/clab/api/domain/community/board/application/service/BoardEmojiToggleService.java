package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.port.in.ToggleBoardEmojiUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardEmoji;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.util.EmojiUtils;

@Service
@RequiredArgsConstructor
public class BoardEmojiToggleService implements ToggleBoardEmojiUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;
    private final RegisterBoardEmojiPort registerBoardEmojiPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public String toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = retrieveBoardPort.getById(boardId);
        BoardEmoji boardEmoji = retrieveBoardEmojiPort.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(existingEmoji -> {
                    existingEmoji.toggleIsDeletedStatus();
                    return existingEmoji;
                })
                .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        registerBoardEmojiPort.save(boardEmoji);
        return board.getCategory().getKey();
    }
}
