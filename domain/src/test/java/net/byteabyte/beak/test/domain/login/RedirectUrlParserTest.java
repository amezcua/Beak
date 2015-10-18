package net.byteabyte.beak.test.domain.login;

import net.byteabyte.beak.domain.oauth.RedirectUrlParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class RedirectUrlParserTest {

  @Test public void tokenAndVerifierAreExtractedFromUrl(){
    String redirectUrl = "http://localhost?oauth_token=test_token&oauth_verifier=test_verifier";

    RedirectUrlParser parser = new RedirectUrlParser();
    RedirectUrlParser.RedirectUserParseResult result = parser.parse(redirectUrl);

    assertEquals(result.getOauthRequestToken(), "test_token");
    assertEquals(result.getOauthVerifier(), "test_verifier");
  }

  @Test(expected = IllegalArgumentException.class) public void emptyRedirectUrlThrows(){
    String redirectUrl = "";

    new RedirectUrlParser().parse(redirectUrl);
  }
}