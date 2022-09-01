package de.fiserv.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class HelperMethods {

  /**
   * returns number of occurrences of the substring sub in string str.
   *
   * @param str string to search in
   * @param sub string to search for
   */
  public static int getOccurrenceOfSub(String str, String sub) {
    return StringUtils.countOccurrencesOf(str, sub);
  }
}
