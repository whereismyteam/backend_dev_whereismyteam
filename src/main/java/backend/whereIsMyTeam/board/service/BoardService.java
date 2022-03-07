package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.repository.*;
import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.exception.Board.*;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.exception.User.UserNotFoundException;
import backend.whereIsMyTeam.result.SingleResult;
import backend.whereIsMyTeam.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeService postLikeService;
    private final CategoryRepository categoryRepository;
    private final AreaRepository areaRepository;

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
        comment.confirmParentEmpty();

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

        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }

    /**
     * 댓글 총 갯수 구하기
     */
    @Transactional
    public GetCommentNumResDto getCommentNum(Long boardIdx) {
        Board board=boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new);
        long commentNum=commentRepository.findCommentNum(board);

        return GetCommentNumResDto.builder()
                .commentNum(commentNum)
                .build();

    }

    /**
     * 게시물 전체 조회
     * @return MainBoardListResDto
     * [조건] : 게시물 상태는 "모집중","모집완료"만 띄워줘야 함 -> 쿼리에서 제대로 선택하도록
     */
    @Transactional
    public List<MainBoardListResponseDto> findAllBoards(Long userIdx,Long categoryIdx) {
        //System.out.print("ㅇㅇㅇ"+ categoryIdx);
        //Board 타입의 해당 카테고리의 글들을 가져옴
        List<Board> boardList = boardRepository.findAllByCategoryIdx(categoryIdx);


        //MainDto 타입의 반환 'List'로 생성
        List<MainBoardListResponseDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList){

            MainBoardListResponseDto newResponseDto;
            //각 게시글마다 댓글 갯수,찜 갯수 받아서 넣어줌.
            long commentNum = commentRepository.findCommentNum(board);
            long heart=postLikeRepository.findPostLikeNum(board.getBoardIdx());

            List<TechStackBoard> techStackBoards=board.getTechstacks();
            List<String> stacks=new ArrayList<>(techStackBoards.size());
            for(int i=0;i<techStackBoards.size();++i){
                stacks.add(i,techStackBoards.get(i).getTechStack().getStackName());
            }
            
            //메인페이지에서 BoardStatus의 (임시저장, 삭제)는 조회되면 안됨.
            if(board.getBoardStatuses().get(0).getCode()==0 || board.getBoardStatuses().get(0).getCode()==3)
                continue;



            //조회 로직 회원,비회원 구분 해야함
            if(userIdx!=0) { //회원
                String isHeart=postLikeService.checkPushedLikeString(userIdx,board.getBoardIdx());
                newResponseDto = new MainBoardListResponseDto(boardRepository.findByBoardIdx(board.getBoardIdx()).orElseThrow(BoardNotExistException::new),
                         stacks,commentNum,heart,isHeart);
                //MainDto로 바꾼 게시글 하나하나씩 List<> 안에 넣어줌
                responseDtoList.add(newResponseDto);
            }
            else{ //비회원
                newResponseDto = new MainBoardListResponseDto(boardRepository.findByBoardIdx(board.getBoardIdx()).orElseThrow(BoardNotExistException::new)
                        ,stacks,heart,commentNum);
                responseDtoList.add(newResponseDto);
            }

        }
        return responseDtoList;
        
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

            List<TechStackBoard> techStackBoards=board.getTechstacks();
            //
            List<String> stacks=new ArrayList<>(techStackBoards.size());
            for(int i=0;i<techStackBoards.size();++i){
                stacks.add(i,techStackBoards.get(i).getTechStack().getStackName());
            }

            long heart=postLikeRepository.findPostLikeNum(boardIdx);
            List<postCommentDto> commentList=postCommentDto.toDtoList(commentRepository.findAllWithUserAndParentByBoardIdxOrderByParentIdxAscNullsFirstCommentIdxAsc(boardIdx));

            //조회 로직 회원,비회원 구분 해야함
            if(userIdx!=0) { //회원
                String isHeart=postLikeService.checkPushedLikeString(userIdx,boardIdx);
                return new GetBoardResponseDto(boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new)
                        ,stacks,heart,isHeart,commentList);
            }
            else{ //비회원
                return new GetBoardResponseDto(boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new),
                        stacks,heart,commentList);
            }

        }
        else {
            throw new NullPointerException();
        }
    }

    /**
     * 게시물 상태 변경 진행
     */

    @Transactional
    public void changeBoardStatus(Long boardIdx,PatchStatusBoardRequestDto requestDto) {
        //게시물 인덱스 검증
        Optional<Board> optional = boardRepository.findByBoardIdx(boardIdx);

        if(optional.isPresent()) { //게시물 존재
            Board board = optional.get();
            //유저랑 게시물 작성자 같은 지 검증
            if(!board.getWriter().getUserIdx().equals(requestDto.getUserIdx())){
                throw new NotWriterException();
            }
            //BoardStatus b=board.getBoardStatuses().get(0);
            //게시물 상태 변경
            board.setBoardStatuses(requestDto.getStatus());
            boardRepository.save(board);
        }

        else{ //게시물 존재 x
            throw new NullPointerException();
        }
    }

    /**
     * 게시물 수정 진행
     */

    @Transactional
    public void updateBoard(Long boardIdx,PatchUpdatePostRequestDto requestDto) {
        //게시물 인덱스 검증
        Optional<Board> optional = boardRepository.findByBoardIdx(boardIdx);

        if(optional.isPresent()) { //게시물 존재
            Board board = optional.get();
            //유저랑 게시물 작성자 같은 지 검증
            if(!board.getWriter().getUserIdx().equals(requestDto.getUserIdx())){
                throw new NotWriterException();
            }
            //게시물 수정
            Category c=categoryRepository.findByCategoryName(requestDto.getCategory()).orElseThrow(WrongInputException::new);
            Area a=areaRepository.findByName(requestDto.getArea()).orElseThrow(WrongInputException::new);

            board.updateBoard(requestDto,c,a);
            boardRepository.save(board);
        }

        else{ //게시물 존재 x 오류 처리
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
