package backend.whereIsMyTeam.exception;

import backend.whereIsMyTeam.constant.ExceptionMessage;
import backend.whereIsMyTeam.exception.User.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice //@Controller로 선언된 메소드 내 예외 처리를 이 핸들러가 하겠다
public class ApiExceptionHandler {


    @ExceptionHandler(value = {UserNotExistException.class})
    public ResponseEntity<Object> handleUserNotExistException(UserNotExistException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.USER_NOT_EXIST_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {UserEmailAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.USER_EMAIL_ALREADY_EXISTS_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {EmailAuthTokenNotFoundException.class})
    public ResponseEntity<Object> handleEmailAuthTokenNotFoundException(EmailAuthTokenNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.EmailAuthToken_Not_Found_Exception_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {UserNickNameAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserNickNameAlreadyExistsException(UserNickNameAlreadyExistsException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.User_NickName_Exists_Exception_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {EmptyNickNameException.class})
    public ResponseEntity<Object> handleEmptyNickNameException(EmptyNickNameException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.EMPTY_NICKNAME_Exception_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.USER_NOT_FOUND_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler(value = {LoginFailureException.class})
    public ResponseEntity<Object> handleLoginFailure(LoginFailureException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.LOGIN_FAILURE_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }





}
