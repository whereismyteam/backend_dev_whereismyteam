package backend.whereIsMyTeam.board;

import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.board.service.BoardService;
import backend.whereIsMyTeam.board.service.PostLikeService;
import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.exception.User.GoToEmailAuthException;
import backend.whereIsMyTeam.exception.User.OnlyUserCanUseException;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.security.jwt.JwtTokenProvider;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.User;
import backend.whereIsMyTeam.user.dto.EmailAuthRequestDto;
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
    private final PostLikeService postLikeService;

    /**
     * 댓글 생성 API
     * [POST] /users/comments/:boardIdx
     * @return SingleResult<NewComment>
     */
    @PostMapping("/comments/{boardIdx}")
    public SingleResult<NewCommentResponseDto> createComment (HttpServletRequest header, @PathVariable Long boardIdx, @Valid @RequestBody NewCommentRequestDto requestDto) {

        if(requestDto.getUserIdx()!=0) {
            //회원 유저인덱스 일치 검증
            User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
            //이메일 인증 검증
            if(!user.getEmailAuth())
                throw new GoToEmailAuthException();
            //access token 검증
            jwtTokenProvider.validateAccess(header, user.getEmail());

            //문제 없으므로 댓글 생성 진행
            NewCommentResponseDto responseDto = boardService.createComment(boardIdx, requestDto);

            return responseService.getSingleResult(responseDto);
        }
        else //유저가 아니므로 사용 불가, 오류 처리
            throw new OnlyUserCanUseException();
    }

    /**
     * 답글 생성 API
     * [POST] /users/comments/:boardIdx/reComments/:parentIdx
     * @return SingleResult<NewComment>
     */
    @PostMapping("/comments/{boardIdx}/reComments/{parentIdx}")
    public SingleResult<NewCommentResponseDto> createReComment (HttpServletRequest header, @PathVariable("boardIdx") Long boardIdx, @PathVariable("parentIdx") Long parentIdx, @Valid @RequestBody NewCommentRequestDto requestDto) {
        
        if(requestDto.getUserIdx()!=0) {
            //회원 유저인덱스 일치 검증
            User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
            //이메일 인증 검증
            if(!user.getEmailAuth())
                throw new GoToEmailAuthException();
            //access token 검증
            jwtTokenProvider.validateAccess(header, user.getEmail());

            //문제 없으므로 답글 생성 진행
            NewCommentResponseDto responseDto=boardService.createReComment(boardIdx,parentIdx,requestDto);

            return responseService.getSingleResult(responseDto);
        }
        else //유저가 아니므로 사용 불가, 오류 처리
            throw new OnlyUserCanUseException();
    }

    /**
     * 댓글/답글 삭제 API
     * [DELETE] /users/comments/:commentIdx
     * @return SingleResult<NewComment>
     */
    @DeleteMapping("/comments/{commentIdx}")
    public SingleResult<String> deleteComment( HttpServletRequest header,@PathVariable("commentIdx") Long commentIdx,@Valid @RequestBody PatchCommentRequestDto requestDto){

        if(requestDto.getUserIdx()!=0) {
            //회원 유저인덱스 일치 검증
            User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
            //이메일 인증 검증
            if(!user.getEmailAuth())
                throw new GoToEmailAuthException();
            //access token 검증
            jwtTokenProvider.validateAccess(header, user.getEmail());

            //문제 없으므로 뎃글, 답글 삭제 진행
            String email=user.getEmail();
            boardService.deleteComment(commentIdx,email);

            return responseService.getSingleResult("댓글 또는 답글이 삭제됐습니다.");
        }
        else //유저가 아니므로 사용 불가, 오류 처리
            throw new OnlyUserCanUseException();
    }

    /**
     * 단건 게시물 조회 API
     * [PUT] users/:userIdx/posts/:postIdx
     * @return SingleResult<String>
     */
    @PutMapping("{userIdx}/posts/{postIdx}")
    public SingleResult<GetBoardResponseDto> getBoardDetail ( HttpServletRequest header, @PathVariable("userIdx") Long userIdx,@PathVariable("postIdx") Long postIdx) {
        if(userIdx!=0){ //회원이라면
            //access token 검증
            User user=userRepository.findByUserIdx(userIdx).orElseThrow(UserNotExistException::new);
            jwtTokenProvider.validateAccess(header, user.getEmail());
        }
        GetBoardResponseDto responseDto=boardService.boardDetail(postIdx,userIdx);
        return responseService.getSingleResult(responseDto);

    }

    /**
     * 찜 생성 API
     * [POST] /users/posts/likes
     * @return SingleResult<String>
     */
    @PostMapping("/posts/likes")
    public SingleResult<PostLikeResponseDto> createPostLike (HttpServletRequest header, @Valid @RequestBody PostLikeRequestDto requestDto) {

        //access token 검증
        User user=userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
        jwtTokenProvider.validateAccess(header, user.getEmail());

        //access 토큰 문제 없으므로 좋아요 등록 or 좋아요 취소
        PostLikeResponseDto responseDto = postLikeService.pushLikeButton(user,requestDto);

//        return responseService.getSingleResult("즐겨찾기에서 해제되었습니다.");
//        return responseService.getSingleResult(responseDto);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 찜 취소 API
     * [POST] /users/posts/cancel/likes
     * @return SingleResult<String>
     */
    @PatchMapping("/posts/cancel/likes")
    public SingleResult<String> cancelPostLike (HttpServletRequest header,  @Valid @RequestBody PostLikeRequestDto requestDto) {

        //access token 검증
        User user=userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
        jwtTokenProvider.validateAccess(header, user.getEmail());

        //access 토큰 문제 없으므로 좋아요 좋아요 취소
        postLikeService.cancelLikeButton(requestDto);

        return responseService.getSingleResult("즐겨찾기에서 해제되었습니다.");

    }

    //좋아요 갯수 실험
    @GetMapping("/posts/likes/not")
    public SingleResult<PostLikeNumResponseDto> checkPostLikeNum (HttpServletRequest header,  @Valid @RequestBody PostLikeNumRequestDto requestDto) {

        //
        PostLikeNumResponseDto responseDto=postLikeService.getPostLikeInfo(requestDto);

        return responseService.getSingleResult(responseDto);
    }

}
