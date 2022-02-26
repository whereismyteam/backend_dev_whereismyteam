package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.PostLike;
import backend.whereIsMyTeam.board.dto.PostLikeRequestDto;
import backend.whereIsMyTeam.board.dto.PostLikeNumRequestDto;
import backend.whereIsMyTeam.board.dto.PostLikeNumResponseDto;
import backend.whereIsMyTeam.board.dto.PostLikeResponseDto;
import backend.whereIsMyTeam.board.repository.BoardRepository;
import backend.whereIsMyTeam.board.repository.PostLikeRepository;
import backend.whereIsMyTeam.exception.Board.BoardNotExistException;
import backend.whereIsMyTeam.exception.Board.PostLikeExistException;
import backend.whereIsMyTeam.exception.Board.PostLikeNotExistException;
import backend.whereIsMyTeam.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    //게시물 찾아오기
    @Override
    @Transactional(readOnly = true)
    public Board getBoard(PostLikeRequestDto postLikeRequestDto) {
        return boardRepository.findByBoardIdx(postLikeRequestDto.getBoardIdx())
                .orElseThrow(BoardNotExistException::new);
    }



    /**
     * 찜 생성
     * @param user
     * @param postLikeRequestDto
     * @return
     **/
    @Override
    public PostLikeResponseDto pushLikeButton(User user, PostLikeRequestDto postLikeRequestDto) {

        PostLike pLike = postLikeRequestDto.toEntity(user, getBoard(postLikeRequestDto));
        if(checkPushedLike(postLikeRequestDto)) {
            /**
             * if 좋아요 클릭
             * (조건) :좋아요 x 여야 함
             * else if  좋아요가 ㅇ? [오류 발생]
             * => FE에서 api를 선택해준 것.
             **/
            throw new PostLikeNotExistException();
        }
        else{
            postLikeRepository.save(pLike);
        }
//                    postLikeRepository.exist(postLikeRequestDto.getUserIdx(), postLikeRequestDto.getBoardIdx())
//                .ifPresentOrElse(
//                        postLike -> {
//                            /**
//                             * if 좋아요 클릭
//                             * (조건) :좋아요 x 여야 함
//                             * else if  좋아요가 ㅇ? [오류 발생]
//                             * => FE에서 api를 선택해준 것.
//                             **/
//                            throw new PostLikeExistException();
//                        }
//                        ,
//                        () -> {
//                            //해당 게시글 좋아요 누른 이력이 x -> 좋아요 누름
//                            //Board board = getBoard(postLikeDto);
//                            postLikeRepository.save(pLike);
//
//                        });

        return PostLikeResponseDto.builder()
                .postLikeIdx(pLike.getLikeIdx())
                .build();
    }

    /**
     * 찜 여부확인
     * @param postLikeRequestDto
     * @return
     **/
    @Transactional(readOnly = true)
    public boolean checkPushedLike(PostLikeRequestDto postLikeRequestDto) {
        return postLikeRepository.exist(postLikeRequestDto.getUserIdx(), postLikeRequestDto.getBoardIdx())
                .isPresent();
    }

    /**
     * 찜 여부확인 String type version
     * @param 'String'
     * @return
     **/
    @Transactional(readOnly = true)
    public String checkPushedLikeString(Long userIdx,Long boardIdx) {
        if(postLikeRepository.exist(userIdx, boardIdx).isPresent()){
            return "Y";
        }
        else{
            return "N";
        }
    }

    /**
     * 찜 취소
     * @param postLikeRequestDto
     * @return
     **/
    @Override
    public Boolean cancelLikeButton( PostLikeRequestDto postLikeRequestDto) {

        postLikeRepository.exist(postLikeRequestDto.getUserIdx(), postLikeRequestDto.getBoardIdx())
                .ifPresentOrElse(
                        //User가 존재(좋아요 누른 이력 ㅇ) -> 좋아요 취소
                        postLike -> {
                            postLikeRepository.deleteBylikeIdx(postLike.getLikeIdx());

                        },
                        () -> {
                            //이미 좋아요 취소를 누른 상태 -> 예외처리
                            throw new PostLikeNotExistException();
                        });
        return true;
    }

    /**
     * 찜 갯수 확인(조회)
     * @param postLikeNumDto
     * @return
     **/
    @Override
    @Transactional(readOnly = true)
    public long getPostLikeNum(PostLikeNumRequestDto postLikeNumDto) {
        //게시글 존재 여부 확인
        boardRepository.findByBoardIdx(postLikeNumDto.getBoardIdx())
                .orElseThrow(BoardNotExistException::new);
        return postLikeRepository.findPostLikeNum(postLikeNumDto.getBoardIdx());
    }


    /**
     * 찜 갯수 출력
     * @param postLikeDto
     * @return
     **/
    @Override
    @Transactional(readOnly = true)
    public PostLikeNumResponseDto getPostLikeInfo( PostLikeNumRequestDto postLikeDto) {

        long postLikeNum = getPostLikeNum(postLikeDto);
        //boolean check = checkPushedLike(postLikeDto);

        return PostLikeNumResponseDto.builder()
                .likeNum(postLikeNum)
                //.likeIdx(pLike.getLikeIdx())
                //.check(check)
                .build();
    }




}