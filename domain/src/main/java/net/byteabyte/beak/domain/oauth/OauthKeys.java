package net.byteabyte.beak.domain.oauth;

public enum OauthKeys {
  OAUTH_CALLBACK("oauth_callback"),
  OAUTH_CALLBACK_CONFIRMED("oauth_callback_confirmed"),
  OAUTH_CONSUMER_KEY("oauth_consumer_key"),
  OAUTH_NONCE("oauth_nonce"),
  OAUTH_SIGNATURE("oauth_signature"),
  OAUTH_SIGNATURE_METHOD("oauth_signature_method"),
  OAUTH_TIMESTAMP("oauth_timestamp"),
  OAUTH_TOKEN("oauth_token"),
  OAUTH_TOKEN_SECRET("oauth_token_secret"),
  OAUTH_VERIFIER("oauth_verifier"),
  OAUTH_VERSION("oauth_version");

  private String key;

  OauthKeys(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }
}
