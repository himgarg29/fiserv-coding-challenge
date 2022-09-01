package de.fiserv.service;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_BAD_REQUEST;
import static de.fiserv.utils.WikiConstants.ERROR_CODE_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_BAD_REQUEST;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_NO_RESULTS;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.FURTHER_INFO_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import de.fiserv.service.impl.WikiRestCaller;
import java.net.URLDecoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class WikiRestCallerTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  WikiRestCaller wikiRestCaller;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(wikiRestCaller, "baseURL", "https://en.wikipedia.org/w/api.php");
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when Wikipedia API sends valid response")
  void getArticleWhenResponseIsValid() {

    String topicName = "some-topic";
    ResponseEntity<String> response = new ResponseEntity<>("{\n"
        + "    \"parse\": {\n"
        + "        \"title\": \"Pasta\",\n"
        + "        \"pageid\": 23871,\n"
        + "        \"text\": {\n"
        + "            \"*\": \"See https://en.wikipedia.org/w/api.php for API usage.\"\n"
        + "        }\n"
        + "    }\n"
        + "}", HttpStatus.OK);
    try {
      when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                  "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=some-topic")
              .build().toUri().toString(), "UTF-8"),
          HttpMethod.GET,
          null,
          String.class))
          .thenReturn(response);

      WikiArticle article = wikiRestCaller.getArticle(topicName);
      assertEquals(23871, article.getPageId());
      assertEquals("Pasta", article.getTitle());
      assertEquals("See https://en.wikipedia.org/w/api.php for API usage.", article.getText());
    } catch (WikiException exp) {
      fail("should have not thrown exception");
    }
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when Wikipedia API sends HTTP code other than 200")
  void getArticleWhenResponseNotHttp200() {
    String topicName = "some-topic";
    ResponseEntity<String> response = new ResponseEntity<>("error", HttpStatus.BAD_GATEWAY);
    try {
      when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                  "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=some-topic")
              .build().toUri().toString(), "UTF-8"),
          HttpMethod.GET,
          null,
          String.class))
          .thenReturn(response);

      wikiRestCaller.getArticle(topicName);
      fail("Should have thrown WikiException");
    } catch (WikiException exp) {
      assertEquals(topicName, exp.getTopicName());
      assertEquals(ERROR_CODE_BAD_REQUEST, exp.getErrorCode());
      assertEquals(String.format(ERROR_MSG_BAD_REQUEST,
          response.getStatusCodeValue()), exp.getErrorMessage());
      assertEquals(FURTHER_INFO_BAD_REQUEST, exp.getFurtherInfo());
    }
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when Wikipedia API sends error message in response")
  void getArticleWhenResponseHasErrorMsg() {
    String topicName = "some-topic";
    ResponseEntity<String> response = new ResponseEntity<>("{\n"
        + "    \"error\": {\n"
        + "        \"code\": \"missingtitle\",\n"
        + "        \"info\": \"The page you specified doesn't exist.\",\n"
        + "        \"*\": \"See https://en.wikipedia.org/w/api.php for API usage.\"\n"
        + "    },\n"
        + "    \"servedby\": \"mw1317\"\n"
        + "}", HttpStatus.OK);
    try {
      when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                  "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=some-topic")
              .build().toUri().toString(), "UTF-8"),
          HttpMethod.GET,
          null,
          String.class))
          .thenReturn(response);

      wikiRestCaller.getArticle(topicName);
      fail("Should have thrown WikiException");
    } catch (WikiException exp) {
      assertEquals(topicName, exp.getTopicName());
      assertEquals("missingtitle", exp.getErrorCode());
      assertEquals(ERROR_MSG_NO_RESULTS, exp.getErrorMessage());
      assertEquals("See https://en.wikipedia.org/w/api.php for API usage.", exp.getFurtherInfo());
    }
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when Wikipedia API sends error message in invalid format")
  void getArticleWhenResponseHasBadFormat() {
    String topicName = "some-topic";
    ResponseEntity<String> response = new ResponseEntity<>("{\n"
        + "    \"parse\": {\n"
        + "        \"title\": \"Pasta\",\n"
        + "        \"pageid\": 23871,\n"
        + "        \"text\": {\n"
        + "            \"*\": \"See https://en.wikipedia.org/w/api.php for API usage.\n"
        + "    }\n"
        + "}", HttpStatus.OK);
    try {
      when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                  "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=some-topic")
              .build().toUri().toString(), "UTF-8"),
          HttpMethod.GET,
          null,
          String.class))
          .thenReturn(response);

      wikiRestCaller.getArticle(topicName);
      fail("Should have thrown WikiException");
    } catch (WikiException exp) {
      assertEquals(topicName, exp.getTopicName());
      assertEquals(ERROR_CODE_RESPONSE_PARSING, exp.getErrorCode());
      assertEquals(ERROR_MSG_RESPONSE_PARSING,
          exp.getErrorMessage());
      assertEquals(
          "Illegal unquoted character ((CTRL-CHAR, code 10)): has to be escaped using backslash to be included in string value",
          exp.getFurtherInfo());
    }
  }
}
