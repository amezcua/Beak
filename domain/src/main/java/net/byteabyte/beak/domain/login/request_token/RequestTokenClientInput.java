package net.byteabyte.beak.domain.login.request_token;

import net.byteabyte.beak.domain.OAuthClientInput;

public class RequestTokenClientInput extends OAuthClientInput {

  private final String redirectUrl;

  public RequestTokenClientInput(String consumerKey, String consumerSecret,
      String redirectUrl){
    super(consumerKey, consumerSecret, null);

    this.redirectUrl = redirectUrl;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

}
