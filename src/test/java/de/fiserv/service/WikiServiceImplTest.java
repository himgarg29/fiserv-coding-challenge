package de.fiserv.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import de.fiserv.model.WikiArticle;
import de.fiserv.service.impl.WikiRestCaller;
import de.fiserv.service.impl.WikiServiceImpl;
import de.fiserv.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WikiServiceImplTest {

  @Mock
  private WikiRestCaller wikiRestCaller;
  @InjectMocks
  WikiServiceImpl wikiService;

  @Test
  @DisplayName("Test to check getArticle method in case of valid topic")
  void getTopicWithValidTopicTest() {
    try {
      String topicName = "some-topic";
      when(wikiRestCaller.getArticle(topicName)).thenReturn(TestUtils.getWikiArticleObject());
      WikiArticle article = wikiService.getTopic(topicName);

      assertEquals(TestUtils.getWikiArticleObject(), article);
    } catch (Exception exp) {
      fail("should have not thrown exception");
    }

  }
}
