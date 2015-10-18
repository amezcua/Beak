package net.byteabyte.beak.domain.login.verify_token;

import net.byteabyte.beak.domain.Action;

public class VerifyTokenAction implements Action<VerifyTokenClientInput, VerifyTokenClientResponse, VerifyTokenClientException> {

  private final VerifyTokenClient verifyTokenClient;
  private VerifyTokenClientInput request;

  public VerifyTokenAction(VerifyTokenClient verifyTokenClient){

    this.verifyTokenClient = verifyTokenClient;
  }

  @Override public void setRequestData(VerifyTokenClientInput request) {
    this.request = request;
  }

  @Override public VerifyTokenClientResponse call() throws VerifyTokenClientException {
    return verifyTokenClient.verifyToken(request);
  }
}
