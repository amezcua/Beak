package net.byteabyte.beak.domain.login.verify_token;

public interface VerifyTokenClient {
  VerifyTokenClientResponse verifyToken(VerifyTokenClientInput verifyTokenClientInput) throws VerifyTokenClientException;
}
