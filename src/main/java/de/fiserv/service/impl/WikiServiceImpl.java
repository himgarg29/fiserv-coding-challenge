package de.fiserv.service.impl;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import de.fiserv.service.WikiService;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WikiServiceImpl implements WikiService {
  private final WikiRestCaller restCaller;
  @Override
  public WikiArticle getTopic(String topicName) throws WikiException, UnsupportedEncodingException {

    return this.restCaller.getArticle(topicName);
  }
}
