package backend.whereIsMyTeam.board;

import backend.whereIsMyTeam.board.dto.NewCommentRequestDto;
import backend.whereIsMyTeam.board.dto.NewCommentResponseDto;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.user.dto.UserLoginRequestDto;
import backend.whereIsMyTeam.user.dto.UserLoginResponseDto;
import backend.whereIsMyTeam.user.dto.UserLogoutRequestDto;
import backend.whereIsMyTeam.user.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BoardService boardService;

    /**
     * 댓글 생성 API
     * [POST] /users/comments
     * @return SingleResult<NewComment>
     */
    @PostMapping("/comments/{boardIdx}")
    public SingleResult<NewCommentResponseDto> createComment (HttpServletRequest header, @PathVariable Long boardIdx, @Valid @RequestBody NewCommentRequestDto requestDto) {

        //access token 검증
        User user=userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
        jwtTokenProvider.validateAccess(header, user.getEmail());

        //access 토큰 문제 없으므로 로그아웃 진행
        NewCommentResponseDto responseDto=boardService.createComment(boardIdx,requestDto);

        return responseService.getSingleResult(responseDto);
    }

}
