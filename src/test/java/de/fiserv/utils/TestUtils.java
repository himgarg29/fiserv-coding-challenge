package de.fiserv.utils;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;

public class TestUtils {

  public static WikiException getWikiExceptionObject() {

    return WikiException.builder()
        .topicName("pasta")
        .errorCode("error-code")
        .errorMessage("error-message")
        .furtherInfo("further-info")
        .build();
  }

  public static WikiArticle getWikiArticleObject() {

    return WikiArticle.builder()
        .pageId(1)
        .title("title for pasta article")
        .text("pasta again pasta")
        .build();
  }

}
