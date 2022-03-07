package backend.whereIsMyTeam.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardStatus {

    /**
     * Enum 명칭: 글 상태
     * 조건: 게시글 작성 때 기본적으로 모집중으로 맞춰야 할듯
     **/

    //상태(모집중, 모집완료, 삭제, 임시저장)
    STORED(0,"임시저장"),
    RECRUITED(1, "모집중"),
    COMPLETED(2,"모집완료"),
    REMOVED(3,"삭제");

    private Integer code;
    private String status;


}
