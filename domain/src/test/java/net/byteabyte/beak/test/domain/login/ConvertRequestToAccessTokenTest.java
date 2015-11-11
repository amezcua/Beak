package net.byteabyte.beak.test.domain.login;

import net.byteabyte.beak.domain.login.verify_token.VerifyTokenAction;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClient;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientException;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientInput;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientResponse;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConvertRequestToAccessTokenTest {

  VerifyTokenClient verifyTokenClient;
  VerifyTokenClientInput mockVerifyTokenClientInput;
  VerifyTokenClientResponse mockVerifyTokenClientResponse;

  @Before public void setup(){
    String consumerKey = "consumer_key";
    String consumerSecret = "consumer_secret";
    String oauthToken = "NPcudxy0yU5T3tBzho7iCotZ3cnetKwcTIRlX0iwRl0";
    String oauthTokenVerifier = OauthKeys.OAUTH_VERIFIER.getKey();
    String oauthTokenSecretResponse = "token_seret";


    mockVerifyTokenClientResponse = new VerifyTokenClientResponse(oauthToken,
        oauthTokenSecretResponse);

    mockVerifyTokenClientInput = new VerifyTokenClientInput(consumerKey, consumerSecret, oauthToken, oauthTokenVerifier);
  }

  @Test public void convertTokenReturnsAccessToken() throws VerifyTokenClientException {

    verifyTokenClient = new VerifyTokenClient() {
      @Override
      public VerifyTokenClientResponse verifyToken(VerifyTokenClientInput verifyTokenClientInput) {
        return mockVerifyTokenClientResponse;
      }
    };

    when(verifyTokenClient.verifyToken(mockVerifyTokenClientInput)).thenReturn(mockVerifyTokenClientResponse);

    VerifyTokenAction action = new VerifyTokenAction(verifyTokenClient);

    VerifyTokenClientResponse verifyTokenActionResponse = action.call(null);

    assertNotNull(verifyTokenActionResponse);
    assertNotNull(verifyTokenActionResponse.getOauthToken());
    assertNotNull(verifyTokenActionResponse.getOauthSecret());
    assertEquals(verifyTokenActionResponse.getOauthToken(), mockVerifyTokenClientInput.getOauthRequestToken());
  }

  @Test(expected = VerifyTokenClientException.class) public void convertWithNullParamsThrows() throws VerifyTokenClientException {

    verifyTokenClient = mock(VerifyTokenClient.class);
    when(verifyTokenClient.verifyToken(null)).thenThrow(VerifyTokenClientException.class);

    VerifyTokenAction action = new VerifyTokenAction(verifyTokenClient);

    action.call(null);
  }
}
