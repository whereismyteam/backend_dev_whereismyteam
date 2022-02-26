package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatchCommentRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    Long userIdx;
}
