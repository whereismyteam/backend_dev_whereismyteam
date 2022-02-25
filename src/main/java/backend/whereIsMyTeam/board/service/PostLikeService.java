package backend.whereIsMyTeam.board.service;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.dto.PostLikeRequestDto;
import backend.whereIsMyTeam.board.dto.PostLikeNumRequestDto;
import backend.whereIsMyTeam.board.dto.PostLikeNumResponseDto;
import backend.whereIsMyTeam.board.dto.PostLikeResponseDto;
import backend.whereIsMyTeam.user.domain.User;

public interface PostLikeService {

    //찜 취소
    public Boolean cancelLikeButton(PostLikeRequestDto postLikeRequestDto);
    //찜 생성
    public PostLikeResponseDto pushLikeButton(User user, PostLikeRequestDto postLikeRequestDto);

    //게시글 가져옴
    public Board getBoard(PostLikeRequestDto postLikeRequestDto);

    //좋아요 갯수
    public long getPostLikeNum(PostLikeNumRequestDto postLikeNumDto);


    //좋아요 여부에 따라
    public PostLikeNumResponseDto getPostLikeInfo( PostLikeNumRequestDto postLikeDto);

    //좋아요 여부
    public boolean checkPushedLike(PostLikeRequestDto postLikeRequestDto);

    //좋아요 여부 string 버전
    public String checkPushedLikeString(Long userIdx,Long boardIdx);



}
