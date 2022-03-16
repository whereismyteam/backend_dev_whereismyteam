package backend.whereIsMyTeam.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRegisterResDto {

    private Long boardIdx;

    @Builder
    public BoardRegisterResDto (Long boardIdx) {
        this.boardIdx = boardIdx;
    }
}
