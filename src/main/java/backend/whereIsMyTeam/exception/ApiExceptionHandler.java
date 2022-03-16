package backend.whereIsMyTeam.exception;

import backend.whereIsMyTeam.constant.ExceptionMessage;
import backend.whereIsMyTeam.exception.Board.*;
import backend.whereIsMyTeam.exception.Jwt.*;
import backend.whereIsMyTeam.exception.User.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

@ControllerAdvice
public class ApiExceptionHandler {

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

    @ExceptionHandler(value = {InvalidRefreshTokenException.class})
    public ResponseEntity<Object> handleInvalidRefreshToken(InvalidRefreshTokenException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.INVALID_REFRESH_Token_MESSAGE,
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
/*
    @ExceptionHandler(value = {EmptyNickNameException.class})
    public ResponseEntity<Object> handleEmptyNickNameException(EmptyNickNameException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.EMPTY_NICKNAME_Exception_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }*/

    @ExceptionHandler({BindException.class})
    public  ResponseEntity<Object> handleBindException(BindException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage());
        }

        ApiException apiException = new ApiException(
                stringBuilder.toString(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public  ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BindingResult bindingResult = e.getBindingResult();
        ApiException apiException = new ApiException(
                bindingResult.getFieldError().getDefaultMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({UserRoleNotExistException.class})
    public  ResponseEntity<Object> handleUserRoleNotExistException(UserRoleNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.ROLE_NOT_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public  ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.ACCESS_DENIED_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({GoToLoginException.class})
    public  ResponseEntity<Object> handleGoToLoginException(GoToLoginException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.GO_LOGIN_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({RefreshNotExistException.class})
    public  ResponseEntity<Object> handleRefreshNotExistException(RefreshNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.REFRESH_NOT_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({InvalidAccessTokenException.class})
    public  ResponseEntity<Object> handleInvalidAccessTokenException(InvalidAccessTokenException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.INVALID_ACCESSTOKEN_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({AccessNotComeException.class})
    public  ResponseEntity<Object> handleAccessNotComeException(AccessNotComeException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.ACCESS_NOT_COME_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({GoToReIssueException.class})
    public  ResponseEntity<Object> handleGoToReIssueException(GoToReIssueException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.GO_TO_REISSUE_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({AuthCodeNotExistException.class})
    public  ResponseEntity<Object> handleAuthCodeNotExistException(AuthCodeNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.AUTH_NOT_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({BoardNotExistException.class})
    public  ResponseEntity<Object> handleBoardNotExistException(BoardNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.BOARD_NOT_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({CommentNotExistException.class})
    public  ResponseEntity<Object> handleCommentNotExistException(CommentNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.COMMENT_NOT_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({NoAuthDeleteCommentException.class})
    public  ResponseEntity<Object> handleNoAuthDeleteCommentException(NoAuthDeleteCommentException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.NO_AUTH_DELETE_COMMENT_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({PostLikeExistException.class})
    public  ResponseEntity<Object> handlePostLikeExistException(PostLikeExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.POSTLIKE_ALREADY_EXIST_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({PostLikeNotExistException.class})
    public  ResponseEntity<Object> handleBoardNotExistException(PostLikeNotExistException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.POSTLIKE_ALREADY_CANCEL_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({CannotConvertNestedStructureException.class})
    public  ResponseEntity<Object> handleCannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.CAN_NOT_CONVERT_NESTED_STRUCTURES_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }
    @ExceptionHandler({GoToEmailAuthException.class})
    public  ResponseEntity<Object> handleGoToEmailAuthException(GoToEmailAuthException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.GET_EMAIL_AUTH_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({OnlyUserCanUseException.class})
    public  ResponseEntity<Object> handleOnlyUserCanUseException(OnlyUserCanUseException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.ONLY_USER_CAN_USE_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({WrongInputException.class})
    public  ResponseEntity<Object> handleWrongInputException(WrongInputException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.WRONG_INPUT_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({NotWriterException.class})
    public  ResponseEntity<Object> handleNotWriterException(NotWriterException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.ONLY_WRITER_CAN_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({NotMatchBoardCateException.class})
    public  ResponseEntity<Object> handleNotMatchBoardCateException(NotMatchBoardCateException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.NOT_MATCH_POSTCATE_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler({NotBoardStatusException.class})
    public  ResponseEntity<Object> handleRefreshNotExistException(NotBoardStatusException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.NOT_BOARD_STATUS_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler({NotMatchMeetingStatusException.class})
    public  ResponseEntity<Object> handleRefreshNotExistException(NotMatchMeetingStatusException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                ExceptionMessage.NOT_MATCH_MEETING_STATUS_EXCEPTION_MESSAGE,
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

}