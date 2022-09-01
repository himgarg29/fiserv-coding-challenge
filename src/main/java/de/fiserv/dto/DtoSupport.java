package de.fiserv.dto;

import de.fiserv.exceptions.WikiException;

/**
 * Support class which provides methods to convert from domain model to Dto model.
 */
public final class DtoSupport {

  private DtoSupport() {
    throw new AssertionError("not for instantiation or inheritance");
  }
  public static ErrorDto fromWikiException(WikiException exception){

    return ErrorDto.builder()
        .searchKeyword(exception.getTopicName())
        .errorCode(exception.getErrorCode())
        .errorMessage(exception.getErrorMessage())
        .furtherInfo(exception.getFurtherInfo())
        .build();
  }

}
