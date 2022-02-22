package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBoardResponseDto {

    private Long boardIdx;
    private Category category; //프로젝트, 스터디, 대회 중 1
    List<TechStackBoard> stackList; //모집하는 스택 기술들 리스트
    private String title;
    private Area area; //지역
    private Long recuritNum;
    private List<MeetingStatus> onOff; //온오프
    private List<String> recruitmentPart;  //모집 파트
    private String postText;
    private List<BoardStatus> boardStatuses; //게시물 상태(삭제,임시저장,모집중,모집완료)
    private postUserDto writer; //게시물 작성자 정보
    private Long watch;
    private Long likeNum;
    List<postCommentDto> commentList; //댓글 부분

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
}
