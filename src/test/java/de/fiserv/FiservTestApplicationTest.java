package de.fiserv;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.ERROR_CODE_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.ERROR_CODE_SERVER_FAILED;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_INVALID_TOPIC;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_NO_RESULTS;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_SERVER_FAILED;
import static de.fiserv.utils.WikiConstants.FURTHER_INFO_INVALID_TOPIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URLDecoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FiservTestApplicationTest {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @MockBean
  private RestTemplate restTemplate;

  @SneakyThrows
  @Test
  @DisplayName("Test when user enters topic for which Wikipedia API returns an article")
  void validTopicTest() {
    ResponseEntity<String> mockedResponse = new ResponseEntity<>("{\n"
        + "    \"parse\": {\n"
        + "        \"title\": \"Pasta\",\n"
        + "        \"pageid\": 23871,\n"
        + "        \"text\": {\n"
        + "            \"*\": \"Pasta Pasta pasta Pasta\"\n"
        + "        }\n"
        + "    }\n"
        + "}", HttpStatus.OK);

    when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=pasta")
            .build().toUri().toString(), "UTF-8"),
        HttpMethod.GET,
        null,
        String.class))
        .thenReturn(mockedResponse);

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=pasta", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("1", response.getBody());
  }

  @Test
  @DisplayName("Test when user enters only whitespace as topic name")
  void emptyTopicTest() {

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=  ", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"searchKeyword\":\"  \","
            + "\"errorCode\":\"" + ERROR_CODE_INVALID_TOPIC + "\","
            + "\"errorMessage\":\"" + ERROR_MSG_INVALID_TOPIC + "\","
            + "\"furtherInfo\":\"" + FURTHER_INFO_INVALID_TOPIC + "\"}",
        response.getBody());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when user enters topic for which Wikipedia API says topic is invalid")
  void invalidTopicTest() {
    ResponseEntity<String> mockedResponse = new ResponseEntity<>("{\n"
        + "    \"error\": {\n"
        + "        \"code\": \"invalidtitle\",\n"
        + "        \"info\": \"Bad title \\\".\\\".\",\n"
        + "        \"*\": \"See https://en.wikipedia.org/w/api.php for API usage.\"\n"
        + "    },\n"
        + "    \"servedby\": \"mw1374\"\n"
        + "}", HttpStatus.OK);

    when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=pasta")
            .build().toUri().toString(), "UTF-8"),
        HttpMethod.GET,
        null,
        String.class))
        .thenReturn(mockedResponse);

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=pasta", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"searchKeyword\":\"pasta\","
            + "\"errorCode\":\"invalidtitle\","
            + "\"errorMessage\":\"" + ERROR_MSG_NO_RESULTS + "\","
            + "\"furtherInfo\":\"See https://en.wikipedia.org/w/api.php for API usage.\"}",
        response.getBody());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when user enters topic for which Wikipedia API doesn't have any article")
  void missingTopicTest() {
    ResponseEntity<String> mockedResponse = new ResponseEntity<>("{\n"
        + "    \"error\": {\n"
        + "        \"code\": \"missingtitle\",\n"
        + "        \"info\": \"The page you specified doesn't exist.\",\n"
        + "        \"*\": \"See https://en.wikipedia.org/w/api.php for API usage.\"\n"
        + "    },\n"
        + "    \"servedby\": \"mw1360\"\n"
        + "}", HttpStatus.OK);

    when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=pasta")
            .build().toUri().toString(), "UTF-8"),
        HttpMethod.GET,
        null,
        String.class))
        .thenReturn(mockedResponse);

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=pasta", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"searchKeyword\":\"pasta\","
            + "\"errorCode\":\"missingtitle\","
            + "\"errorMessage\":\"" + ERROR_MSG_NO_RESULTS + "\","
            + "\"furtherInfo\":\"See https://en.wikipedia.org/w/api.php for API usage.\"}",
        response.getBody());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when user enters topic for which Wikipedia API sends a response which is not parseable")
  void responseNotParseableTest() {
    ResponseEntity<String> mockedResponse = new ResponseEntity<>("{\n"
        + "    \"parse\": {\n"
        + "        \"title\": \"Pasta\",\n"
        + "        \"pageid\": 23871,\n"
        + "        \"text\": {\n"
        + "            \"*\" \"Pasta Pasta pasta Pasta\"\n"
        + "        }\n"
        + "    }\n"
        + "}", HttpStatus.OK);

    when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=pasta")
            .build().toUri().toString(), "UTF-8"),
        HttpMethod.GET,
        null,
        String.class))
        .thenReturn(mockedResponse);

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=pasta", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"searchKeyword\":\"pasta\","
            + "\"errorCode\":\"" + ERROR_CODE_RESPONSE_PARSING + "\","
            + "\"errorMessage\":\"" + ERROR_MSG_RESPONSE_PARSING + "\","
            + "\"furtherInfo\":\"Unexpected character ('\\\"' (code 34)): was expecting a colon to separate field name and value\"}",
        response.getBody());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test when an exception is thrown while calling Wikipedia API")
  void exceptionTest() {
    when(restTemplate.exchange(URLDecoder.decode(UriComponentsBuilder.fromHttpUrl(
                "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=pasta")
            .build().toUri().toString(), "UTF-8"),
        HttpMethod.GET,
        null,
        String.class)).thenThrow(new RuntimeException("exception occured while calling Wikipedia"));

    ResponseEntity<String> response = testRestTemplate.
        getForEntity("/wiki/topic?name=pasta", String.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("{\"errorCode\":\"" + ERROR_CODE_SERVER_FAILED + "\","
            + "\"errorMessage\":\"" + ERROR_MSG_SERVER_FAILED + "\","
            + "\"furtherInfo\":\"exception occured while calling Wikipedia\"}",
        response.getBody());
  }

}
