package net.byteabyte.beak.domain.login.verify_token;

import net.byteabyte.beak.domain.OAuthClientInput;

public class VerifyTokenClientInput extends OAuthClientInput {

  private final String oauthVerifier;

  public VerifyTokenClientInput(String consumerKey, String consumerSecret, String oauthRequestToken,
      String oauthVerifier){
    super(consumerKey, consumerSecret, oauthRequestToken);

    this.oauthVerifier = oauthVerifier;
  }

  public String getOauthVerifier() {
    return oauthVerifier;
  }
}