package org.henick.repoinfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException exception) {
        GithubErrorExternalDto githubError = exception.getResponseBodyAs(GithubErrorExternalDto.class);
        ErrorResponse response = new ErrorResponse(
                exception.getStatusCode().value(),
                githubError != null ? githubError.message() : exception.getStatusText()
        );

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

}
