package de.fiserv.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode // added for testing purpose
public class WikiArticle {

  private final String title;
  private final int pageId;
  private final String text;

}
