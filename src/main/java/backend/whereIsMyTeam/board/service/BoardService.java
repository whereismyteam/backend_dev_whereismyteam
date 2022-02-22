package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.repository.*;
import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.exception.Board.*;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Dto로 들어온 값을 통해 댓글 작성 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public NewCommentResponseDto createComment(Long postIdx, NewCommentRequestDto requestDto) {

        Comment comment = requestDto.toEntity();

        comment.confirmWriter(userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new));
        comment.confirmBoard(boardRepository.findByBoardIdx(postIdx).orElseThrow(BoardNotExistException::new));

        commentRepository.save(comment);

        return NewCommentResponseDto.builder()
                .commentIdx(comment.getCommentIdx())
                .build();
    }

  //  void removeComment (Long commentIdx) throws CommentException;


//    /**
//     *
//     * 게시글 작성
//     **/
//    @Transactional
//    public BoardRegisterReqDto saveBoard(BoardRegisterReqDto reqDto){
//
//        //임시저장이냐 등록이냐에 따라 status 분리
//        Board board = BoardRepository.save(
//                Board.builder()
//                        .category(reqDto.getCategory())
//                        .area(reqDto.getArea())
//                        .capacityNum(reqDto.getCapacityNum())
//                        .title(reqDto.getTitle())
//                        .content(reqDto.getContent())
//                        .writer(reqDto.getUser())
//                        .boardStatus(reqDto.getStatus())
////                        .nickName(requestDto.getNickName())
////                        .roles(Collections.singletonList(Role.ROLE_NOTAUTH))
////                        .provider(null)
//                        .build());
//
//        return BoardRegisterResDto.builder()
//                .boardIdx(board.getBoardIdx())
//                .build();
//    }
//}
}
