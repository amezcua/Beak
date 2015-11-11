package net.byteabyte.beak.login.verify_token;

import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClient;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientException;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientInput;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientResponse;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthRetrofitClient;
import net.byteabyte.beak.network.common.TwitterService;
import retrofit.Response;

public class VerifyTokenClientImp extends OAuthRetrofitClient implements VerifyTokenClient {

  @Override
  public VerifyTokenClientResponse verifyToken(VerifyTokenClientInput verifyTokenClientInput)
      throws VerifyTokenClientException {

    try {
      Response<ResponseBody> twitterResponse = buildTwitterService(verifyTokenClientInput).
          verifyLoginRequestToken(verifyTokenClientInput.getOauthVerifier()).execute();

      return buildOutputResponse(twitterResponse);
    }catch (IOException | NullPointerException e){
      throw new VerifyTokenClientException(e);
    }
  }

  private TwitterService buildTwitterService(VerifyTokenClientInput verifyTokenClientInput){
    OAuthClientParameters
        params = new OAuthClientParameters(verifyTokenClientInput.getConsumerKey(), verifyTokenClientInput.getConsumerSecret(), verifyTokenClientInput.getOauthRequestToken());

    return buildOauthRetrofitClient(params).create(TwitterService.class);
  }

  private VerifyTokenClientResponse buildOutputResponse(Response<ResponseBody> response)
      throws IOException, NullPointerException {

    String body = response.body().string();
    String[] parts = body.split("&");

    Map<String, String> partsMap = new HashMap<>();
    for(String part: parts){
      String[] kv = part.split("=");
      partsMap.put(kv[0], kv[1]);
    }

    return new VerifyTokenClientResponse(
        partsMap.get(OauthKeys.OAUTH_TOKEN.getKey()),
        partsMap.get(OauthKeys.OAUTH_TOKEN_SECRET.getKey()));
  }
}