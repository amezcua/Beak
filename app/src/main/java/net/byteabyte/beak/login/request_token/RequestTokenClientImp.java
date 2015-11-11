package net.byteabyte.beak.login.request_token;

import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClient;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientException;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientInput;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientResponse;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthRetrofitClient;
import net.byteabyte.beak.network.common.TwitterService;
import retrofit.Response;

public class RequestTokenClientImp extends OAuthRetrofitClient implements RequestTokenClient {

  @Override public RequestTokenClientResponse requestToken(RequestTokenClientInput requestTokenClientInput) throws RequestTokenClientException {

    try {
      Response<ResponseBody> twitterResponse = buildTwitterService(requestTokenClientInput).getLoginRequestToken().execute();
      return buildOutputResponse(twitterResponse);
    }catch (IOException e){
      throw new RequestTokenClientException(e);
    }
  }

  private TwitterService buildTwitterService(RequestTokenClientInput requestTokenClientInput){
    Map<String, String> extraParams = new HashMap<>();
    extraParams.put(OauthKeys.OAUTH_CALLBACK.getKey(), requestTokenClientInput.getRedirectUrl());
    OAuthClientParameters
        params = new OAuthClientParameters(requestTokenClientInput.getConsumerKey(),
        requestTokenClientInput.getConsumerSecret(), extraParams);

    return buildOauthRetrofitClient(params).create(TwitterService.class);
  }

  private RequestTokenClientResponse buildOutputResponse(Response<ResponseBody> response)
      throws IOException {
    String body = response.body().string();
    String[] parts = body.split("&");

    Map<String, String> partsMap = new HashMap<>();
    for(String part: parts){
      String[] kv = part.split("=");
      partsMap.put(kv[0], kv[1]);
    }

    return new RequestTokenClientResponse(
        partsMap.get(OauthKeys.OAUTH_TOKEN.getKey()),
        partsMap.get(OauthKeys.OAUTH_TOKEN_SECRET.getKey()),
        partsMap.get(OauthKeys.OAUTH_CALLBACK_CONFIRMED.getKey()).equalsIgnoreCase("true"));
  }
}
