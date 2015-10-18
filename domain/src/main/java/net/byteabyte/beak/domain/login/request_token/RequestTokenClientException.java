package net.byteabyte.beak.domain.login.request_token;

public class RequestTokenClientException extends Exception {
  public RequestTokenClientException(Throwable inner){
    super(inner);
  }
}
