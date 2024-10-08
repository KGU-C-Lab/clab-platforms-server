package page.clab.api.domain.community.board.application.dto.response;

import lombok.Getter;

@Getter
public class WriterInfo {

    private final String id;
    private final String name;
    private Long roleLevel;
    private String imageUrl;

    public WriterInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public WriterInfo(String id, String name, Long roleLevel, String imageUrl) {
        this.id = id;
        this.name = name;
        this.roleLevel = roleLevel;
        this.imageUrl = imageUrl;
    }
}
