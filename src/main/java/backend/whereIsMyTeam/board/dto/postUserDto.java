package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.user.domain.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
//@Setter
public class postUserDto {
    private Long userIdx;
    private Long profileImgIdx;
    private String userName;

    public postUserDto(User user){
        this.userIdx=user.getUserIdx();
        this.profileImgIdx=user.getProfileImgIdx();
        this.userName=user.getNickName();
    }
}
