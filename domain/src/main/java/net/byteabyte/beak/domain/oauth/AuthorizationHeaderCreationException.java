package net.byteabyte.beak.domain.oauth;

public class AuthorizationHeaderCreationException extends Throwable {
  public AuthorizationHeaderCreationException(){}

  public AuthorizationHeaderCreationException(Throwable parent){
    super(parent);
  }
}