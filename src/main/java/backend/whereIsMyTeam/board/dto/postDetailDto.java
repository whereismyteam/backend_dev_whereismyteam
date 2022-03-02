package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.MeetingStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postDetailDto {
    private String location;//지역
    private Long number; //모집인원
    private String onOff;
    private List<String> parts;  //모집 파트

    public postDetailDto(Board post){
        this.location=post.getArea().getName();
        this.number=post.getCapacityNum();
        this.onOff=post.getMeetingStatuses().get(0).getStatus();
        this.parts=post.getRecruitmentPart();

    }
}
