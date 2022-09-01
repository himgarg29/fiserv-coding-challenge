package de.fiserv.exceptions;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WikiException extends Exception {

  private final String topicName;
  private final String errorCode;
  private final String errorMessage;
  private final String furtherInfo;
}
