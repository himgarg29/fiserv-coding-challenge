package de.fiserv.service;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import java.io.UnsupportedEncodingException;

public interface WikiService {

  /**
   * returns an article from Wikipedia with the given topic
   *
   * @param topicName topic to search for
   * @throws WikiException in case if no response or response received from Wikipedia API is not valid
   * @throws UnsupportedEncodingException in case, if character encoding needs to be consulted, but
   * named character encoding is not supported
   */
  WikiArticle getTopic(String topicName) throws WikiException, UnsupportedEncodingException;
}
