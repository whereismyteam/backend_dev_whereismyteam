//package backend.whereIsMyTeam.domain.service;
//
//import backend.whereIsMyTeam.domain.Board;
//import backend.whereIsMyTeam.domain.BoardStatus;
//import backend.whereIsMyTeam.domain.dto.BoardRegisterReqDto;
//import backend.whereIsMyTeam.domain.dto.BoardRegisterResDto;
//import backend.whereIsMyTeam.domain.repository.BoardRepository;
//import backend.whereIsMyTeam.user.domain.Role;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.mybatis.logging.Logger;
//import org.mybatis.logging.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//
//@Slf4j
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class BoardService {
//
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final BoardRepository boardRepository;
//
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