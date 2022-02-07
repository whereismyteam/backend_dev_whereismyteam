package backend.whereIsMyTeam.constant;

import lombok.Getter;

@Getter
public class ExceptionMessage {
    public static final String USER_NOT_EXIST_MESSAGE = "유저가 존재하지 않습니다.";
    public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "중복된 이메일입니다.";
    public static final String  EmailAuthToken_Not_Found_Exception_MESSAGE = "이매일 인증 토큰을 찾을 수 없습니다.";
    public static final String User_NickName_Exists_Exception_MESSAGE= "중복된 닉네임입니다.";
    //public static final String EMPTY_NICKNAME_Exception_MESSAGE= "닉네임을 입력해주세요.";
    public static final String ROLE_NOT_EXIST_EXCEPTION_MESSAGE= "해당 유저의 인증여부를 찾을 수 없습니다.";
    public static final String EMPTY_NICKNAME_Exception_MESSAGE= "닉네임을 입력해주세요.";

    public static final String USER_NOT_FOUND_MESSAGE = "입력하신 이메일을 찾을 수 없습니다.";
    public static final String LOGIN_FAILURE_MESSAGE = "입력하신 비밀번호는 잘못된 비밀번호 입니다.";

}
