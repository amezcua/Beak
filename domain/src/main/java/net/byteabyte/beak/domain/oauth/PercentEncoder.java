package net.byteabyte.beak.domain.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class PercentEncoder {
  /**
   * From http://oauth.googlecode.com/svn/code/java/core/commons/src/main/java/net/oauth/OAuth.java
   */
  public static String encode(String s) {
    if (s == null) {
      return "";
    }
    try {
      return URLEncoder.encode(s, "UTF-8")
          // OAuth encodes some characters differently:
          .replace("+", "%20").replace("*", "%2A")
          .replace("%7E", "~");
      // This could be done faster with more hand-crafted code.
    } catch (UnsupportedEncodingException wow) {
      throw new RuntimeException(wow.getMessage(), wow);
    }
  }
}
