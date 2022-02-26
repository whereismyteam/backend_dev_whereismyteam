package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.repository.*;
import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.exception.Board.*;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
       // Optional<Comment> empty=Optional.ofNullable(comment);
        comment.confirmParentEmpty();

        return NewCommentResponseDto.builder()
                .commentIdx(comment.getCommentIdx())
                .build();
        /*
        User user = userRepository.findByUserIdx(requestDto.getUserIdx()).orElseThrow(UserNotExistException::new);
        Board board = boardRepository.findByBoardIdx(postIdx).orElseThrow(BoardNotExistException::new);
        Comment parent = Optional.ofNullable(null)

        Comment comment = commentRepository.save(new Comment(requestDto.getContent(),requestDto.getIsSecret(), user, board, parent));
        comment.publishCreatedEvent(publisher);*/
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

        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }

    /**
     * 게시물 단건 조회
     * @return GetBoardResponseDto
     */
    @Transactional
    public GetBoardResponseDto boardDetail(Long boardIdx,Long userIdx) {
        Optional<Board> optional = boardRepository.findByBoardIdx(boardIdx);
        if(optional.isPresent()) {
            Board board = optional.get();
            //방문자 수 1 증가
            board.setHitCnt(board.getHitCnt() + 1);
            boardRepository.save(board);
            //조회 로직 회원,비회원 구분 해야함
            if(userIdx!=0) { //회원
                return new GetBoardResponseDto(boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new));
            }
            else{ //비회원
                return new GetBoardResponseDto(0,boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new));
            }

        }
        else {
            throw new NullPointerException();
        }
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
