package backend.whereIsMyTeam.board.dto;


import backend.whereIsMyTeam.board.NestedConvertHelper;
import backend.whereIsMyTeam.board.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
//@Setter
public class postCommentDto {
    private Long commentIdx;
    private String isSecret;
    private String comment;
    private postUserDto member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<postCommentDto> children;

    public static List<postCommentDto> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                c -> new postCommentDto(c.getCommentIdx(),c.getIsSecret(),c.getContent(),new postUserDto(c.getUser()), c.getCreateAt(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getCommentIdx(),
                d -> d.getChildren());
        return helper.convert();
    }

}
