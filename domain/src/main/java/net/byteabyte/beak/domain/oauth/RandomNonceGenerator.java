package net.byteabyte.beak.domain.oauth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

class RandomNonceGenerator implements NonceGenerator {
  @Override public String createNonce() throws NoSuchAlgorithmException {
    SecureRandom random = new SecureRandom();

    byte seed[] = random.generateSeed(10);
    MessageDigest md;
    md = MessageDigest.getInstance("SHA-512");
    byte sha[] = Arrays.copyOf(md.digest(seed), 32);

    return HexTools.toHexString(sha);
  }
}
