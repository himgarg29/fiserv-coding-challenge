package de.fiserv.controller;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.FURTHER_INFO_INVALID_TOPIC;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import de.fiserv.service.WikiService;
import de.fiserv.utils.HelperMethods;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller which counts how many times a topic appears in the Wikipedia's article's text field.
 */
@Slf4j
@RestController
@RequestMapping("/wiki")
public class WikiController {

  private final WikiService wikiService;

  @Autowired
  public WikiController(WikiService wikiService) {
    this.wikiService = wikiService;
  }

  @GetMapping(value = "/topic")
  public ResponseEntity<Integer> getTopicCount(@RequestParam("name") String topicName)
      throws WikiException, UnsupportedEncodingException {

    log.debug("<getTopicCount> topic received: {}", topicName);
    if (!StringUtils.hasText(topicName)) {
      throw WikiException.builder()
          .topicName(topicName)
          .errorCode(ERROR_CODE_INVALID_TOPIC)
          .errorMessage(ERROR_MSG_INVALID_TOPIC)
          .furtherInfo(FURTHER_INFO_INVALID_TOPIC)
          .build();
    }

    final WikiArticle topic = this.wikiService.getTopic(topicName);
    log.debug("<getTopicCount> topic found with title: {}", topic.getTitle());

    final int count = HelperMethods.getOccurrenceOfSub(topic.getText(), topicName);
    return ResponseEntity.ok(count);

  }
}
