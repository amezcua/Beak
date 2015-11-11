package net.byteabyte.beak.domain.login.request_token;

import net.byteabyte.beak.domain.Action;

public class GetLoginRequestTokenAction implements Action<RequestTokenClientInput, RequestTokenClientResponse, RequestTokenClientException> {

  private final RequestTokenClient requestTokenClient;

  public GetLoginRequestTokenAction(RequestTokenClient requestTokenClient){
    this.requestTokenClient = requestTokenClient;
  }

  @Override public RequestTokenClientResponse call(RequestTokenClientInput request) throws RequestTokenClientException {
    return requestTokenClient.requestToken(request);
  }
}
