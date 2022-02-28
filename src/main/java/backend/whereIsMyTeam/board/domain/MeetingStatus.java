package backend.whereIsMyTeam.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingStatus {

    /**
     * 1. Table 명: '회의방식'
     * 2. 조건)
     **/


    //회의방식(온라인, 오프라인, 온/오프)
    ONLINE(0,"온라인"),
    OFFLINE(1, "오프라인"),
    BLENDED(2,"온/오프");

    private Integer code;
    private String status;
}
