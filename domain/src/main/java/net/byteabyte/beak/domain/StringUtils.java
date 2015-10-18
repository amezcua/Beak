package net.byteabyte.beak.domain;

public class StringUtils {
  public static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().length() ==0;
  }
}