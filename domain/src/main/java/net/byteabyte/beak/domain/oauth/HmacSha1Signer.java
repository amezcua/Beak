package net.byteabyte.beak.domain.oauth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class HmacSha1Signer {
  private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

  public static byte[] calculateSignature(String data, String key)
      throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
  {
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
    Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
    mac.init(signingKey);
    return mac.doFinal(data.getBytes());
  }
}
