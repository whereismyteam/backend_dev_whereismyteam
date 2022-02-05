package backend.whereIsMyTeam.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterResponseDto {

    private Integer userIdx;

    private String email;

    @Builder
    public UserRegisterResponseDto(Integer userIdx, String email) {
        this.userIdx = userIdx;
        this.email = email;
    }
}
