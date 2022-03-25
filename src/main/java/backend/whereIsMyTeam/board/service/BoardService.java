package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.repository.*;
import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.dto.*;
import backend.whereIsMyTeam.exception.Board.*;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import backend.whereIsMyTeam.user.UserRepository;
import backend.whereIsMyTeam.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final TechStackRepository techStackRepository;
    private final TechStackBoardRepository techStackBoardRepository;

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
     * 기술스택(string)으로 해당 기술idx를 가진
     * -> List<Long> boardIdx  목록으로 반환
     **/
    public List<Long> findBoardListIdxs(searchRequestParams reqdto){

        //boardIdx 중복 제거
        Set<Long> setboardIdxs = new HashSet<>();

        for (int i=0; i<reqdto.getTechStacks().size(); ++i){
            //스택 이름으로 tstackIdx 가져오기
            TechStack ts = techStackRepository.findByStackName(reqdto.getTechStacks().get(i));
            //stackIdx 받아오기(해당 스택이름의)
            Long stackIds = ts.getStackIdx();
            System.out.println("해당 기술스택의 stackIdx 받아오기 " + stackIds);

            //[문제] 쿼리 문제
            List<Board> tsbs = techStackBoardRepository.findByTechStackIdx(stackIds);

            for (int j=0; j< tsbs.size(); j++){
                Long boardIdx = tsbs.get(j).getBoardIdx();
                System.out.println("지금 들어온 boardIdx는 " + boardIdx);
                setboardIdxs.add(boardIdx);
            }

        }

        List<Long> boardIdxs = new ArrayList<>(setboardIdxs);
        return boardIdxs;
    }




    /**
     * 게시물 전체 조회 (스택 검사 x)
     * @return MainBoardListResDto
     * [조건] : 게시물 상태는 "모집중","모집완료"만 띄워줘야 함 -> 쿼리에서 제대로 선택하도록
     */
    @Transactional
    public List<MainBoardListResponseDto> findAllBoards(Long userIdx,Long categoryIdx, /*Boolean created,*/ Boolean liked,Boolean meeting,
                                                        int size, Long lastArticleIdx) {
        //Board 타입의 해당 카테고리의 글들을 가져옴
        //[문제]size가 0-2까지 결과를 넣었을때 원하는 결과가 출력이 안됨.
        Pageable pageable = PageRequest.of(0,size);

        //List<Board> boardList;

        Page<Board> boardList ;

        if (liked)
        {
            if(lastArticleIdx==0){ //처음으로 조회했을때(0으로 처리해도 되는지 고려)
                boardList = boardRepository.findAllByCategoryIdxAndLiked(categoryIdx,pageable);
            }
            else{
                boardList = boardRepository.findAllByCategoryIdxAndLikedWithLastIdx(categoryIdx, lastArticleIdx, pageable);
            }

        }else{ //기본정렬(최신순)->카테고리 Idx로만 내보냄
            if(lastArticleIdx==0) {
                boardList = boardRepository.findAllByCategoryIdxAndCreateAt(categoryIdx,pageable);
            }else{
                boardList = boardRepository.findAllByCategoryIdxAndCreateAtWithLastIdx(categoryIdx,lastArticleIdx,pageable);
            }

        }

        //[문제]: 게시글이 더이상 없을 경우
        if (boardList.getContent().size() == 0) {
            throw new LastBoardExistException();
        }
        //매번 size만큼 받아와 MainDto 타입의 반환 'List'로 생성
        List<MainBoardListResponseDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList.getContent()){
            //메인페이지에서 BoardStatus의 (임시저장, 삭제)는 조회되면 안됨.
            if(board.getBoardStatuses().get(0).getCode()==0 || board.getBoardStatuses().get(0).getCode()==3){
                continue;
            }
            if(meeting && board.getBoardStatuses().get(0).getCode()==2){
                //'모집중'만 선택시 , list에서 모집완료는 배제
                continue;
            }

            MainBoardListResponseDto newResponseDto;

            //각 게시글마다 댓글 갯수,찜 갯수 받아서 넣어줌.
            long commentNum = commentRepository.findCommentNum(board);
            long heart=postLikeRepository.findPostLikeNum(board.getBoardIdx());

            List<TechStackBoard> techStackBoards=board.getTechstacks();
            List<String> stacks=new ArrayList<>(techStackBoards.size());
            for(int i=0;i<techStackBoards.size();++i){
                stacks.add(i,techStackBoards.get(i).getTechStack().getStackName());
            }


            //조회 로직 회원,비회원 구분 해야함
            if(userIdx!=0) { //회원
                String isHeart=postLikeService.checkPushedLikeString(userIdx,board.getBoardIdx());
                newResponseDto = new MainBoardListResponseDto(board,stacks,commentNum,heart,isHeart);
                //MainDto로 바꾼 게시글 하나하나씩 List<> 안에 넣어줌
                responseDtoList.add(newResponseDto);
            }
            else{ //비회원
                newResponseDto = new MainBoardListResponseDto(board,stacks,heart,commentNum);
                responseDtoList.add(newResponseDto);
            }

        }

        return responseDtoList;
        
    }

    /**
     * 기술 스택 포함한 전체 글 조회
     **/

    @Transactional
    public List<MainBoardListResponseDto> findAllBoardsWithStack(Long userIdx,Long categoryIdx, /*Boolean created,*/ Boolean liked,Boolean meeting,
                                                        int size, Long lastArticleIdx,
                                                                 List<Long> boardIdxst) {
        //Board 타입의 해당 카테고리의 글들을 가져옴
        //size가 0-2까지 결과를 넣었을때 원하는 결과가 출력이 안됨.
        Pageable pageable = PageRequest.of(0,size);


        //List<Board> boardList;
        System.out.println("boardIdxst 값들은 " + boardIdxst);

        Page<Board> boardList ;


        if (liked) {
            if(lastArticleIdx==0) { //처음으로 조회했을때(0으로 처리해도 되는지 고려)
                boardList = boardRepository.findAllByCategoryIdxAndLikedAndStacks(categoryIdx,boardIdxst,pageable);
            } else{
                boardList = boardRepository.findAllByCategoryIdxAndLikedWithLastIdxAndStacks(categoryIdx, lastArticleIdx,boardIdxst, pageable);
            }

        }else{ //기본정렬(최신순)->카테고리 Idx로만 내보냄
            if(lastArticleIdx==0) {
                boardList = boardRepository.findAllByCategoryIdxAndCreateAtAndStacks(categoryIdx,boardIdxst,pageable);
            }else{
                boardList = boardRepository.findAllByCategoryIdxAndCreateAtWithLastIdxAndStacks(categoryIdx,lastArticleIdx,boardIdxst,pageable);
            }

        }


        //뒤에 오는 게시글이 이제 없을 경우
        if (boardList.getContent().size()== 0) {
            throw new LastBoardExistException();
        }

        //MainDto 타입의 반환 'List'로 생성
        List<MainBoardListResponseDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList.getContent()){
            //메인페이지에서 BoardStatus의 (임시저장, 삭제)는 조회되면 안됨.
            if(board.getBoardStatuses().get(0).getCode()==0 || board.getBoardStatuses().get(0).getCode()==3){
                continue;
            }
            if(meeting && board.getBoardStatuses().get(0).getCode()==2){
                //'모집중'만 선택시 , list에서 모집완료는 배제
                continue;
            }

            MainBoardListResponseDto newResponseDto;

            //각 게시글마다 댓글 갯수,찜 갯수 받아서 넣어줌.
            long commentNum = commentRepository.findCommentNum(board);
            long heart=postLikeRepository.findPostLikeNum(board.getBoardIdx());

            List<TechStackBoard> techStackBoards=board.getTechstacks();
            List<String> stacks=new ArrayList<>(techStackBoards.size());
            for(int i=0;i<techStackBoards.size();++i){
                stacks.add(i,techStackBoards.get(i).getTechStack().getStackName());
            }



            //조회 로직 회원,비회원 구분 해야함
            if(userIdx!=0) { //회원
                String isHeart=postLikeService.checkPushedLikeString(userIdx,board.getBoardIdx());
                newResponseDto = new MainBoardListResponseDto(board,stacks,commentNum,heart,isHeart);
                //MainDto로 바꾼 게시글 하나하나씩 List<> 안에 넣어줌
                responseDtoList.add(newResponseDto);
            }
            else{ //비회원
                newResponseDto = new MainBoardListResponseDto(board,stacks,heart,commentNum);
                responseDtoList.add(newResponseDto);
            }

        }
        //이러고 맨 마지막 페이지에 hasNext 해줘야 할거 같음
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

            //기존 스택리스트 삭제
            for(int i=0;i<board.getTechstacks().size();++i){
              TechStackBoard deleted=  board.getTechstacks().get(i);
              techStackBoardRepository.delete(deleted);

            }


            List<TechStackBoard> techstacks=new ArrayList<>(requestDto.getTechstacks().size());
            //새로운 스택리스트 주입
            for(int i=0;i<requestDto.getTechstacks().size();++i){
                TechStack ts=techStackRepository.findByStackName(requestDto.getTechstacks().get(i));
                TechStackBoard t = new TechStackBoard(ts,board);
                techstacks.add(t);
                techStackBoardRepository.save(techstacks.get(i));
            }

            board.updateBoard(requestDto,c,a);
            boardRepository.save(board);
        }

        else{ //게시물 존재 x 오류 처리
            throw new NullPointerException();
        }
    }

    /**
     * 임시 저장 게시물 단건 조회
     * @return GetPrePostResDto
     */
    @Transactional
    public GetPrePostResDto PreBoardDetail(Long userIdx,Long boardIdx) {
        Optional<Board> optional = boardRepository.findByBoardIdx(boardIdx);
        if(optional.isPresent()) {
            Board board = optional.get();
            //유저랑 게시물 작성자 같은 지 검증
            if(!board.getWriter().getUserIdx().equals(userIdx)){
                throw new NotWriterException();
            }
            List<TechStackBoard> techStackBoards=board.getTechstacks();
            List<String> stacks=new ArrayList<>(techStackBoards.size());
            for(int i=0;i<techStackBoards.size();++i){
                stacks.add(i,techStackBoards.get(i).getTechStack().getStackName());
            }
            return new GetPrePostResDto(boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotExistException::new),stacks);

        }
        else {
            throw new NullPointerException();
        }
    }


    /**
     * 임시 저장 삭제 진행
     */

    @Transactional
    public void deletePrePost(Long boardIdx,PatchPrePostReqDto requestDto) {
        //게시물 인덱스 검증
        Optional<Board> optional = boardRepository.findByBoardIdx(boardIdx);

        if(optional.isPresent()) { //게시물 존재
            Board board = optional.get();
            //유저랑 게시물 작성자 같은 지 검증
            if(!board.getWriter().getUserIdx().equals(requestDto.getUserIdx())){
                throw new NotWriterException();
            }

            board.setBoardStatuses("삭제");
            boardRepository.save(board);
        }

        else{ //게시물 존재 x
            throw new NullPointerException();
        }
    }

    /**
     * 임시저장 게시물 목록 전체 조회
     * @return GetPrePostListResDto
     * [조건] : 게시물 상태는 "임시저장"
     */
    @Transactional
    public GetPrePostListResDto findPreBoards(User user ) {

        long num=0;

        //해당 유저 임시 저장 게시물들 가져오기
        List<Board> boardList = boardRepository.findByWriter(user);


        //MainDto 타입의 반환 'List'로 생성
        List<prePostInfoDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList){

            //임시 저장 아니면 진행 X
            if(!(board.getBoardStatuses().get(0).getCode()==0 ))
                continue;

            prePostInfoDto newResponseDto;

            newResponseDto = new prePostInfoDto(board.getTitle(),board.getCreateAt());

            responseDtoList.add(newResponseDto);
            num++;
        }
        //게시물 전체수 조회

        return new GetPrePostListResDto(num,responseDtoList);

    }


    /**
     * 임시저장/기본 게시글 작성(등록)
     * @return BoardRegisterResDto
     * [조건] : 게시물 상태: '모집 중',"임시저장"
     **/
    @Transactional
    public BoardRegisterResDto createdBoard(BoardRegisterReqDto reqDto, User user){

        //카테고리 선정
        Optional<Category> optional=categoryRepository.findByCategoryName(reqDto.getCategoryName());
        Category category = optional.get();

        //지역 선정
         Area area = areaRepository.findByName(reqDto.getArea()).orElseThrow(WrongInputException::new);

        //회의방식(onOff)
        List<MeetingStatus> m =new ArrayList<>();
        if(reqDto.getOnOff().equals("온라인")) {
           m = Collections.singletonList(MeetingStatus.ONLINE);
        }else if(reqDto.getOnOff().equals("오프라인")) {
            m = Collections.singletonList(MeetingStatus.OFFLINE);
            // this.meetingStatuses.set(0, MeetingStatus.OFFLINE);
        }else{
            if(reqDto.getOnOff().equals("온/오프")) {
                m = Collections.singletonList(MeetingStatus.BLENDED);
            }else //그 외에 결과가 들어왔을 때
                throw new NotMatchMeetingStatusException();
        }


        //글 상태(BoardStatus)
        List<BoardStatus> b =new ArrayList<>();
        if(reqDto.getBoardStatus().equals("임시저장")) {
            b = Collections.singletonList(BoardStatus.STORED);
        }else if(reqDto.getBoardStatus().equals("모집중")) {
            b = Collections.singletonList(BoardStatus.RECRUITED);
        }else{//임시저장 or 모집중인 글만 작성해야 함
            throw new NotBoardStatusException();
        }


        //임시저장이냐 등록이냐에 따라 status 분리
        Board board = boardRepository.save(
                Board.builder()
                        .categorys(category)
                        .areas(area)
                        .capacityNum(reqDto.getCapacityNum())
                        .title(reqDto.getTitle())
                        .content(reqDto.getContent())
                        .writer(user)
                        .recruitmentPart(reqDto.getRecruitmentPart())
                        .meetingStatus(m) //collections로 처리
                        .boardStatus(b)
                        //.techstackss(techstacks) //필요없음
                        .build());

        List<TechStackBoard> techstacks=new ArrayList<>(reqDto.getTechstacks().size());
        //새로운 스택리스트 주입
        for(int i=0; i<reqDto.getTechstacks().size();++i){
            TechStack ts=techStackRepository.findByStackName(reqDto.getTechstacks().get(i));
            TechStackBoard t = new TechStackBoard(ts,board);
            techstacks.add(t);
            techStackBoardRepository.save(techstacks.get(i));
        }

        return BoardRegisterResDto.builder()
                .boardIdx(board.getBoardIdx())
                .build();
    }


    /**
     * 게시글 삭제
     * [조건] : 게시물 상태: '모집 중'
     * 1. 게시글 기술스택 제대로 체크했는지 확인 필요하지 않을까?
     **/
//    @Transactional
//    public void deletePost(Long postIdx, PatchPostReqDto reqDto){
//
//        Optional<Board> optional = boardRepository.findByBoardIdx(postIdx);
//
//        if(optional.isPresent()) { //게시물 여부 확인
//            Board board = optional.get();
//            //작성자와 삭제 요청자가 같은지 확인
//            if (reqDto.getUserIdx() != board.getWriter().getUserIdx()) {
//                throw new NotWriterException();
//            }
//
//            //게시글 삭제
//            boardRepository.delete(board);
//
//        }else{ //게시물 존재 x
//            throw new NullPointerException();
//        }
//    }

}
