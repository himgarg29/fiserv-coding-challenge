package de.fiserv.controller;


import static de.fiserv.utils.WikiConstants.ERROR_CODE_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.FURTHER_INFO_INVALID_TOPIC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import de.fiserv.service.WikiService;
import de.fiserv.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WikiController.class)
class WikiControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private WikiService wikiService;

  @Test
  @DisplayName("Test to check with valid title")
  void getTopicCountWithValidTitle() throws Exception {
    WikiArticle article = TestUtils.getWikiArticleObject();

    when(wikiService.getTopic(any(String.class))).thenReturn(article);
    mvc.perform(get("/wiki/topic?name=pasta"))
        .andExpect(status().isOk())
        .andExpect(content().string("2"));

    verify(wikiService).getTopic(any(String.class));
  }

  @Test
  @DisplayName("Test to check with empty title")
  void getTopicCountWithEmptyTitle() throws Exception {
    mvc.perform(get("/wiki/topic?name= "))
        .andExpect(status().isOk())
        .andExpect(content().string(
            "{\"searchKeyword\":\" \","
                + "\"errorCode\":\""+ERROR_CODE_INVALID_TOPIC+"\","
                + "\"errorMessage\":\""+ERROR_MSG_INVALID_TOPIC+"\","
                + "\"furtherInfo\":\""+FURTHER_INFO_INVALID_TOPIC+"\"}"));
  }

  @Test
  @DisplayName("Test to check with invalid title")
  void getTopicCountWithInvalidTitle() throws Exception {
    WikiException exception = TestUtils.getWikiExceptionObject();
    doThrow(exception).when(wikiService).getTopic(any(String.class));

    mvc.perform(get("/wiki/topic?name=pasta"))
        .andExpect(status().isOk())
        .andExpect(content().string(
            "{\"searchKeyword\":\"pasta\","
                + "\"errorCode\":\"error-code\","
                + "\"errorMessage\":\"error-message\","
                + "\"furtherInfo\":\"further-info\"}"));

    verify(wikiService).getTopic(any(String.class));
  }
}
