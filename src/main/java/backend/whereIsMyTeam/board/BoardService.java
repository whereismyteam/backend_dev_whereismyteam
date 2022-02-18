package backend.whereIsMyTeam.board;

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

import java.util.List;

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

    /**
     * Dto로 들어온 값을 통해 답글 작성 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public NewCommentResponseDto createReComment(Long boardIdx, Long parentIdx, NewCommentRequestDto requestDto) {
        Comment comment = requestDto.toEntity();

        comment.confirmWriter(userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new));
        comment.confirmBoard(boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new));
        comment.confirmParent(commentRepository.findByCommentIdx(parentIdx).orElseThrow(CommentNotExistException::new));

        commentRepository.save(comment);

        return NewCommentResponseDto.builder()
                .commentIdx(comment.getCommentIdx())
                .build();

    }

    /**
     * 댓글 또는 답글 삭제 진행
     */
    @Transactional
    public void deleteComment(Long commentIdx,String email) {

        Comment comment = commentRepository.findByCommentIdx(commentIdx).orElseThrow(CommentNotExistException::new);


        //댓글 또는 답글 작성한 본인 아니면 예외 처리
        if(!comment.getUser().getEmail().equals(email))
            throw new NoAuthDeleteCommentException();

        comment.delete();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }



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
