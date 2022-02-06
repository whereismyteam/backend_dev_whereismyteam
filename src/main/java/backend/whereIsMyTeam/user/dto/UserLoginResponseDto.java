package backend.whereIsMyTeam.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {
    private Long userIdx;
    private String token;
    private String refreshToken;
}
