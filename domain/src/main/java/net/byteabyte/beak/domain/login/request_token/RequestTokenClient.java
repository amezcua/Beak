package net.byteabyte.beak.domain.login.request_token;

public interface RequestTokenClient {
  RequestTokenClientResponse requestToken(RequestTokenClientInput requestTokenClientInput) throws
      RequestTokenClientException;
}
