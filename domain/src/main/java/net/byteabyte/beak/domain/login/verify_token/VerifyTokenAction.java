package net.byteabyte.beak.domain.login.verify_token;

import net.byteabyte.beak.domain.Action;

public class VerifyTokenAction implements Action<VerifyTokenClientInput, VerifyTokenClientResponse, VerifyTokenClientException> {

  private final VerifyTokenClient verifyTokenClient;

  public VerifyTokenAction(VerifyTokenClient verifyTokenClient){
    this.verifyTokenClient = verifyTokenClient;
  }

  @Override public VerifyTokenClientResponse call(VerifyTokenClientInput request) throws VerifyTokenClientException {
    return verifyTokenClient.verifyToken(request);
  }
}
