package net.byteabyte.beak.domain.login.request_token;

import net.byteabyte.beak.domain.Twitter;
import net.byteabyte.beak.domain.oauth.OauthKeys;

public class RequestTokenClientResponse {
  private final String oAuthToken;
  private final String oAuthTokenSecret;
  private final boolean oAuthCallbackConfirmed;

  public RequestTokenClientResponse(String oAuthToken, String oAuthTokenSecret,
      boolean oAuthCallbackConfirmed) {
    this.oAuthToken = oAuthToken;
    this.oAuthTokenSecret = oAuthTokenSecret;
    this.oAuthCallbackConfirmed = oAuthCallbackConfirmed;
  }

  public String buildLoginUrl() {
    return Twitter.AUTHENTICATE_URL + "?" + OauthKeys.OAUTH_TOKEN.getKey() + "=" + oAuthToken;
  }
}
