package de.fiserv.exceptions;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_SERVER_FAILED;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_SERVER_FAILED;

import de.fiserv.dto.DtoSupport;
import de.fiserv.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Consolidate multiple Exceptions into a single, global error handling component.
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler({WikiException.class})
  @ResponseStatus(HttpStatus.OK)
  public ErrorDto customException(WikiException exp) {
    log.warn("<customException> For topic: {}, Exception occurred while processing request: {}", exp.getTopicName(),
        exp.getErrorMessage());
    return DtoSupport.fromWikiException(exp);
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto uncheckedException(Exception exp) {
    log.warn("<uncheckedException> Exception occurred while processing request: {}",
        exp.getMessage());

    return ErrorDto.builder()
        .errorCode(ERROR_CODE_SERVER_FAILED)
        .errorMessage(ERROR_MSG_SERVER_FAILED)
        .furtherInfo(exp.getMessage())
        .build();
  }
}
