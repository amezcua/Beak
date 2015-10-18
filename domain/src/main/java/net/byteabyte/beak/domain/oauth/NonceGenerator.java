package net.byteabyte.beak.domain.oauth;

import java.security.NoSuchAlgorithmException;

public interface NonceGenerator {
  String createNonce() throws NoSuchAlgorithmException;
}
