package backend.whereIsMyTeam.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardStatus {
    //상태(모집중, 모집완료, 삭제, 임시저장)
    STORED(0,"임시저장"),
    RECRUITED(1, "모집중"),
    COMPLETED(2,"모집완료"),
    REMOVEED(3,"삭제");

    private Integer code;
    private String status;


}
