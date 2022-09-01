package de.fiserv.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

class HelperMethodsTest {

  @SneakyThrows
  @Test
  @DisplayName("Test to check occurrence of small and capital chars")
  void getOccuranceOfSubTest() {

    String file = StreamUtils.copyToString(
        Files.newInputStream(new File("src/test/resources/pizza-article.txt").toPath()),
        Charset.defaultCharset());

    int count = HelperMethods.getOccurrenceOfSub(file, "pizza");
    assertEquals(166, count);

    count = HelperMethods.getOccurrenceOfSub(file, "Pizza");
    assertEquals(152, count);
  }

  @SneakyThrows
  @Test
  @DisplayName("Test to check occurrence of encoded chars")
  void specialCharOccurrenceTest() {

    String s = "Ä00c4ä\u00e4";
    assertEquals(2, HelperMethods.getOccurrenceOfSub(s, "ä"));
    assertEquals(1, HelperMethods.getOccurrenceOfSub(s, "Ä"));
  }
}
