package de.fiserv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorDto {

  private final String searchKeyword;
  private final String errorCode;
  private final String errorMessage;
  private final String furtherInfo;

}
