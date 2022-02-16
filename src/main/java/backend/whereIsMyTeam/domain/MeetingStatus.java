package backend.whereIsMyTeam.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingStatus {

    //회의방식(온라인, 오프라인, 온/오프)
    ONLINED(0,"온라인"),
    OFFLINED(1, "오프라인"),
    BLENDED(2,"온/오프");

    private Integer code;
    private String status;
}
