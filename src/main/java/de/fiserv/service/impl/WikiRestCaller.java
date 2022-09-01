package de.fiserv.service.impl;

import static de.fiserv.utils.WikiConstants.ERROR_CODE_BAD_REQUEST;
import static de.fiserv.utils.WikiConstants.ERROR_CODE_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_BAD_REQUEST;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_NO_RESULTS;
import static de.fiserv.utils.WikiConstants.ERROR_MSG_RESPONSE_PARSING;
import static de.fiserv.utils.WikiConstants.FURTHER_INFO_BAD_REQUEST;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import de.fiserv.exceptions.WikiException;
import de.fiserv.model.WikiArticle;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Component class for calling Wikipedia APIs.
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WikiRestCaller {

  @Value("${downstream.wiki.baseUrl:https://en.wikipedia.org/w/api.php}")
  private String baseURL;
  private final RestTemplate restTemplate;

  /**
   * Returns an article by calling Wikipedia GET API.
   *
   * @param topicName topic to search for
   * @throws WikiException in case Wikipedia API sends a Http status code other than 200, or if
   * Wikipedia API sends an exception or if the response from Wikipedia API is not parseable
   * @throws UnsupportedEncodingException in case, if character encoding needs to be consulted, but
   * * named character encoding is not supported
   */
  public WikiArticle getArticle(String topicName)
      throws WikiException, UnsupportedEncodingException {
    final URI serviceUri = UriComponentsBuilder.fromHttpUrl(this.baseURL)
        .queryParam("action", "parse")
        .queryParam("section", "0")
        .queryParam("prop", "text")
        .queryParam("format", "json")
        .queryParam("page", topicName)
        .build().toUri();

    try {
      final String finalUrl =
          URLDecoder.decode(serviceUri.toString(), "UTF-8"); //decoding url for special chars
      log.info("<getArticle> fetching article with title: {} from: {}", topicName, finalUrl);
      final ResponseEntity<String> response = this.restTemplate.exchange(finalUrl, HttpMethod.GET,
          null, String.class);

      if (response.getStatusCode() != HttpStatus.OK) {
        throw WikiException.builder()
            .topicName(topicName)
            .errorCode(ERROR_CODE_BAD_REQUEST)
            .errorMessage(String.format(ERROR_MSG_BAD_REQUEST,
                response.getStatusCodeValue()))
            .furtherInfo(FURTHER_INFO_BAD_REQUEST)
            .build();
      }

      final ObjectMapper mapper = new ObjectMapper();
      final String responseText = response.getBody();
      log.info("<getArticle> response received from: {} for title: {} {}", finalUrl, topicName, responseText);

      final JsonNode errorNode = mapper.readTree(responseText).path("error");

      if (errorNode.getNodeType() != JsonNodeType.MISSING) {
        throw WikiException.builder()
            .topicName(topicName)
            .errorCode(errorNode.path("code").asText())
            .errorMessage(ERROR_MSG_NO_RESULTS)
            .furtherInfo(errorNode.path("*").asText())
            .build();

      } else {
        final JsonNode name = mapper.readTree(responseText).path("parse");
        return WikiArticle.builder()
            .pageId(name.path("pageid").asInt())
            .title(name.path("title").asText())
            .text(name.path("text").path("*")
                .asText()) //also converts encoding for special chars to char
            .build();
      }

    } catch (JsonProcessingException e) {
      throw WikiException.builder()
          .topicName(topicName)
          .errorCode(ERROR_CODE_RESPONSE_PARSING)
          .errorMessage(ERROR_MSG_RESPONSE_PARSING)
          .furtherInfo(e.getOriginalMessage())
          .build();
    }
  }
}
