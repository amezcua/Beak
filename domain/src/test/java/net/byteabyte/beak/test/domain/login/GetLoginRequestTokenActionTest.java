package net.byteabyte.beak.test.domain.login;

import net.byteabyte.beak.domain.login.request_token.GetLoginRequestTokenAction;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClient;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientException;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientInput;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetLoginRequestTokenActionTest {
  @Test public void actionReturnsTokens() throws RequestTokenClientException {
    final RequestTokenClientResponse expectedResponse = new RequestTokenClientResponse("MOCK_TOKEN", "OAUTH_SECRET", true);

    RequestTokenClient mockRequestTokenClient = new RequestTokenClient() {
      @Override
      public RequestTokenClientResponse requestToken(RequestTokenClientInput requestTokenClientInput)
          throws RequestTokenClientException {
        return expectedResponse;
      }
    };

    RequestTokenClientResponse actualResponse = new GetLoginRequestTokenAction(mockRequestTokenClient).call(null);

    assertEquals(expectedResponse, actualResponse);
  }
}