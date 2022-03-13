package backend.whereIsMyTeam.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetPrePostListResDto {
    //총 수
    //게시물 제목
    //임시저장 날짜
    private Long totalNum;

    private List<prePostInfoDto> prePostList;

    @Builder
    public GetPrePostListResDto(long totalNum,List<prePostInfoDto> prePostList ) {
        this.totalNum = totalNum;
        this.prePostList=prePostList;
    }


}
