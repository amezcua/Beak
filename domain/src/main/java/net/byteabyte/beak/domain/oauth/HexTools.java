package net.byteabyte.beak.domain.oauth;

import java.util.Formatter;

public class HexTools {
  public static String toHexString(byte[] bytes) {
    Formatter formatter = new Formatter();

    for (byte b : bytes) {
      formatter.format("%02x", b);
    }

    return formatter.toString().toUpperCase();
  }
}