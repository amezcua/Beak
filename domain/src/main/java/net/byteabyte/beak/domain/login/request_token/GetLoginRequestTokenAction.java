package net.byteabyte.beak.domain.login.request_token;

import net.byteabyte.beak.domain.Action;

public class GetLoginRequestTokenAction implements Action<RequestTokenClientInput, RequestTokenClientResponse, RequestTokenClientException> {

  private final RequestTokenClient requestTokenClient;
  private String consumerKey;
  private String consumerSecret;
  private String oauthCallback;

  public GetLoginRequestTokenAction(RequestTokenClient requestTokenClient){
    this.requestTokenClient = requestTokenClient;
  }

  @Override public void setRequestData(RequestTokenClientInput requestInput) {
    this.consumerKey = requestInput.getConsumerKey();
    this.consumerSecret = requestInput.getConsumerSecret();
    this.oauthCallback = requestInput.getRedirectUrl();
  }

  @Override public RequestTokenClientResponse call() throws RequestTokenClientException {
    return requestTokenClient.requestToken(new RequestTokenClientInput(consumerKey, consumerSecret, oauthCallback));
  }
}
