package de.fiserv.exceptions;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_SERVER_FAILED;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_SERVER_FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.fiserv.dto.ErrorDto;
import de.fiserv.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomExceptionHandlerTest {

  @Test
  @DisplayName("Test to check server response in case of any custom exception")
  void customExceptionTest(){

    WikiException exception = TestUtils.getWikiExceptionObject();
    CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();
    ErrorDto dto = exceptionHandler.customException(exception);

    assertEquals(exception.getTopicName(), dto.getSearchKeyword());
    assertEquals(exception.getErrorCode(), dto.getErrorCode());
    assertEquals(exception.getErrorMessage(), dto.getErrorMessage());
    assertEquals(exception.getFurtherInfo(), dto.getFurtherInfo());
  }

  @Test
  @DisplayName("Test to check server response in case of any unchecked exception")
  void uncheckedExceptionTest(){

    Exception exp = new RuntimeException("runtime exception occurred");
    CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();
    ErrorDto dto = exceptionHandler.uncheckedException(exp);

    assertEquals(ERROR_CODE_SERVER_FAILED, dto.getErrorCode());
    assertEquals(ERROR_MSG_SERVER_FAILED, dto.getErrorMessage());
    assertEquals(exp.getMessage(), dto.getFurtherInfo());
  }

}
