package net.byteabyte.beak.network.common;

import java.util.Map;
import net.byteabyte.beak.domain.StringUtils;

public class OAuthClientParameters {
  private final String consumerKey;
  private final String consumerSecret;
  private final String oauthAccessToken ;
  private final String oauthAccessTokenSecret;
  private final Map<String, String> extraParameters;

  public OAuthClientParameters(String consumerKey, String consumerSecret) {
    this(consumerKey, consumerSecret, null, null, null);
  }

  public OAuthClientParameters(String consumerKey, String consumerSecret, String oauthAccessToken) {
    this(consumerKey, consumerSecret, oauthAccessToken, null, null);
  }

  public OAuthClientParameters(String consumerKey, String consumerSecret, String oauthAccessToken, String oauthAccessTokenSecret) {
    this(consumerKey, consumerSecret, oauthAccessToken, oauthAccessTokenSecret, null);
  }

  public OAuthClientParameters(String consumerKey, String consumerSecret,
      Map<String, String> extraParameters) {
    this(consumerKey, consumerSecret, null, null, extraParameters);
  }

  public OAuthClientParameters(String consumerKey, String consumerSecret, String oauthAccessToken,
      String oauthAccessTokenSecret, Map<String, String> extraParameters) {
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.oauthAccessToken = oauthAccessToken;
    this.oauthAccessTokenSecret = oauthAccessTokenSecret;
    this.extraParameters = extraParameters;
  }

  public boolean hasOauthToken(){
    return !StringUtils.isNullOrEmpty(getOauthAccessToken());
  }

  public String getConsumerKey() {
    return consumerKey;
  }

  public String getConsumerSecret() {
    return consumerSecret;
  }

  public String getOauthAccessToken() {
    return oauthAccessToken;
  }

  public String getOauthAccessTokenSecret() {
    return oauthAccessTokenSecret;
  }

  public Map<String, String> getExtraParameters() {
    return extraParameters;
  }
}
