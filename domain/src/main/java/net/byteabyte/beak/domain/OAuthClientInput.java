package net.byteabyte.beak.domain;

public abstract class OAuthClientInput {
  private final String consumerKey;
  private final String consumerSecret;
  private final String oauthRequestToken;
  private final String oauthSecret;

  protected OAuthClientInput(String consumerKey, String consumerSecret, String oauthRequestToken) {
    this(consumerKey, consumerSecret, oauthRequestToken, null);
  }

  protected OAuthClientInput(String consumerKey, String consumerSecret, String oauthRequestToken, String oauthSecret) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.oauthRequestToken = oauthRequestToken;
    this.oauthSecret = oauthSecret;
  }

  public String getConsumerKey() {
    return consumerKey;
  }

  public String getConsumerSecret() {
    return consumerSecret;
  }

  public String getOauthRequestToken() {
    return oauthRequestToken;
  }

  public String getOauthSecret() {
    return oauthSecret;
  }
}
