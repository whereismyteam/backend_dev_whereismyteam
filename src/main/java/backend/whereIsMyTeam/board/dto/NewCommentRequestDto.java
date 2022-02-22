package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCommentRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    Long userIdx;

    @NotNull(message = "댓글 내용을 입력해주세요.")
    String content;

    @NotNull(message = "비밀댓글 여부를 입력해주세요.")
    String isSecret;

    public Comment toEntity() {

        return Comment.builder()
                .content(content)
                .isSecret(isSecret).build();
    }

}
