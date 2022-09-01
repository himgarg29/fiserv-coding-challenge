package de.fiserv.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WikiConstants {

  public static final String ERROR_CODE_INVALID_TOPIC = "Invalid title";
  public static final String ERROR_CODE_BAD_REQUEST = "Bad request";
  public static final String ERROR_CODE_RESPONSE_PARSING = "Bad response format";
  public static final String ERROR_CODE_SERVER_FAILED = "Server error";
  public static final String ERROR_MSG_INVALID_TOPIC = "Title entered is either null or empty";
  public static final String ERROR_MSG_BAD_REQUEST =
      "Request at Wikipedia API failed. HttpCode received: %d";
  public static final String ERROR_MSG_RESPONSE_PARSING =
      "Exception occurred while parsing the response received from Wikipedia API";
  public static final String ERROR_MSG_NO_RESULTS = "No result found for entered topic";
  public static final String ERROR_MSG_SERVER_FAILED = "Server failed to process the request";
  public static final String FURTHER_INFO_INVALID_TOPIC =
      "Please try again with a title which has atleast 1 character";
  public static final String FURTHER_INFO_BAD_REQUEST = "Please try again after some time";


}
