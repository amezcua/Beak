package net.byteabyte.beak.domain.oauth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

class SignatureGenerator {

  private String signingKey;

  public SignatureGenerator(String consumerSecret, String tokenSecret){
    this.signingKey = createSigningKey(consumerSecret, tokenSecret);
  }

  public String computeHmacSha1(String value)
      throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
    return Base64.encodeToString(HmacSha1Signer.calculateSignature(value, signingKey), Base64.NO_WRAP);
  }

  private String createSigningKey(String consumerSecret, String tokenSecret) {
    return PercentEncoder.encode(consumerSecret) + "&" + PercentEncoder.encode(tokenSecret);
  }
}
