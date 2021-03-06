package backend.whereIsMyTeam.constant;

import lombok.Getter;

@Getter
public class ExceptionMessage {
    public static final String USER_NOT_EXIST_MESSAGE = "유저가 존재하지 않습니다.";
    public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "중복된 이메일입니다.";
    public static final String EmailAuthToken_Not_Found_Exception_MESSAGE = "인증에 실패했습니다. 이매일 인증 토큰을 찾을 수 없습니다.";
    public static final String User_NickName_Exists_Exception_MESSAGE = "중복된 닉네임입니다.";

    public static final String ROLE_NOT_EXIST_EXCEPTION_MESSAGE = "해당 유저의 인증여부를 찾을 수 없습니다.";
    public static final String EMPTY_NICKNAME_Exception_MESSAGE = "닉네임을 입력해주세요.";

    public static final String USER_NOT_FOUND_MESSAGE = "입력하신 이메일을 찾을 수 없습니다.";
    public static final String LOGIN_FAILURE_MESSAGE = "입력하신 비밀번호는 잘못된 비밀번호 입니다.";
    


    public static String INVALID_REFRESH_Token_MESSAGE = "Refresh 토큰이 유효하지 않습니다.";
    public static String ACCESS_DENIED_EXCEPTION_MESSAGE = "Access 토큰이 만료되지 않았습니다.";

    //access reIssue validation
    public static String GO_LOGIN_EXCEPTION_MESSAGE = "다시 로그인 해주세요.";
    public static String REFRESH_NOT_EXIST_EXCEPTION_MESSAGE = "Refresh 토큰이 오지 않았습니다.";

    //access validatoin in every api after login
    public static String INVALID_ACCESSTOKEN_EXCEPTION_MESSAGE = "Access 토큰이 유효하지 않습니다.";
    public static String ACCESS_NOT_COME_EXCEPTION_MESSAGE = "Access 토큰이 오지 않았습니다.";
    public static String GO_TO_REISSUE_EXCEPTION_MESSAGE = "Access 토큰이 만료됐습니다. 재발급 받아주세요.";

    //소셜 로그인
    public static String AUTH_NOT_EXIST_EXCEPTION_MESSAGE = "인증 코드를 입력해주세요";

    //댓글 작성
    public static String BOARD_NOT_EXIST_EXCEPTION_MESSAGE = "게시물이 존재하지 않습니다.";

    //대댓글 작성
    public static String COMMENT_NOT_EXIST_EXCEPTION_MESSAGE = "댓글이 존재하지 않습니다.";

    //댓글 삭제
    public static String NO_AUTH_DELETE_COMMENT_EXCEPTION_MESSAGE = "댓글은 작성자만 삭제할 수 있습니다.";

    //게시물 조회
    public static String CAN_NOT_CONVERT_NESTED_STRUCTURES_EXCEPTION_MESSAGE= "댓글 계층 구조를 전환하는데 실패했습니다.";

    //찜 생성
    public static final String POSTLIKE_ALREADY_EXIST_EXCEPTION_MESSAGE = "이미 좋아요를 누른 글입니다";

    //찜 취소
    public static final String POSTLIKE_ALREADY_CANCEL_EXCEPTION_MESSAGE = "이미 좋아요를 취소한 글입니다.";
    
    //API 사용 검증
    public static String GET_EMAIL_AUTH_EXCEPTION_MESSAGE="이메일 인증을 해주세요.";
    public static String ONLY_USER_CAN_USE_MESSAGE="회원만 사용가능한 API 입니다.";

    //게시물 상태 변경
    public static String NOT_MATCH_POSTCATE_MESSAGE="임시 저장 게시물 구별이 필요합니다.";
    public static String WRONG_INPUT_MESSAGE="잘못된 요청 양식입니다.";
    public static String ONLY_WRITER_CAN_MESSAGE="글 작성자 외엔 권한이 없습니다.";
    public static final String NOT_BOARD_STATUS_EXCEPTION_MESSAGE = "BOARD STATUS 에러입니다";
    public static String NOT_MATCH_MEETING_STATUS_EXCEPTION_MESSAGE = "MEETING STATUS를 제대로 기입해주세요.";
    public static String LAST_BOARD_EXCEPTION_MESSAGE = "더이상 조회되는 게시글이 없습니다.";
}