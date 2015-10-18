package net.byteabyte.beak.domain.login.verify_token;

public class VerifyTokenClientException extends Exception {
  public VerifyTokenClientException() {
  }

  public VerifyTokenClientException(Exception innerException) {
    super(innerException);
  }
}
