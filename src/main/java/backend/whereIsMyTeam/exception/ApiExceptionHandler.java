package backend.whereIsMyTeam.exception;

import backend.whereIsMyTeam.constant.ExceptionMessage;
import backend.whereIsMyTeam.exception.User.EmailAuthTokenNotFoundException;
import backend.whereIsMyTeam.exception.User.UserEmailAlreadyExistsException;
import backend.whereIsMyTeam.exception.User.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
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

}
