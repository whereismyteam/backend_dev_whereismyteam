package backend.whereIsMyTeam.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterResponseDto {

    private Long userIdx;

    private String email;

    @Builder
    public UserRegisterResponseDto(Long userIdx, String email) {
        this.userIdx = userIdx;
        this.email = email;
    }
}
