package de.fiserv.dto;

import de.fiserv.exceptions.WikiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoSupportTest {

  @Test
  @DisplayName("Test to check the conversion from domain to dto model")
  void fromWikiExceptionTest() {

    String topicName = "topic-name";
    String errorCode = "error-code";
    String errorMsg = "error-msg";
    String furtherInfo = "further-info";

    WikiException exception = WikiException.builder()
        .topicName(topicName)
        .errorCode(errorCode)
        .errorMessage(errorMsg)
        .furtherInfo(furtherInfo)
        .build();

    ErrorDto dto = DtoSupport.fromWikiException(exception);
    assertEquals(topicName, dto.getSearchKeyword());
    assertEquals(errorCode, dto.getErrorCode());
    assertEquals(errorMsg, dto.getErrorMessage());
    assertEquals(furtherInfo, dto.getFurtherInfo());

  }

}
