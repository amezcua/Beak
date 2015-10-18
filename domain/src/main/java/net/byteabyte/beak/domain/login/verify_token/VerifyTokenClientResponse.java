package net.byteabyte.beak.domain.login.verify_token;

public class VerifyTokenClientResponse {
  private String oauthToken;
  private String oauthSecret;

  public VerifyTokenClientResponse(String oauthToken, String oauthSecret) {
    this.oauthToken = oauthToken;
    this.oauthSecret = oauthSecret;
  }

  public String getOauthToken() {
    return oauthToken;
  }

  public String getOauthSecret() {
    return oauthSecret;
  }
}
