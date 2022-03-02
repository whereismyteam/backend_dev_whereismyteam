package backend.whereIsMyTeam.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCommentNumResDto {

    private Long commentNum;

    @Builder
    public GetCommentNumResDto(Long commentNum) {
        this.commentNum = commentNum;
    }
}
