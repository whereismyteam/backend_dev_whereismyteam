package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.user.domain.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postUserDto {
    private Long userIdx;
    //private Long profileImgIdx; //userImg로 바꿔야함
    private String userImg;
    private String userName;
    private Boolean emailAuth;

    public postUserDto(User user){
        this.userIdx=user.getUserIdx();
        //this.userImg=user.getProfileImgIdx();
        this.userName=user.getNickName();
        this.emailAuth=user.getEmailAuth();
    }
}
