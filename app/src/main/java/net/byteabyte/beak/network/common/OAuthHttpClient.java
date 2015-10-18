package net.byteabyte.beak.network.common;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import net.byteabyte.beak.domain.oauth.AuthorizationHeader;
import net.byteabyte.beak.domain.oauth.AuthorizationHeaderCreationException;

public class OAuthHttpClient extends OkHttpClient {

  private static final String TAG = "OauthClient";
  private static final String HEADER_AUTHORIZATION = "Authorization";

  private final OAuthClientParameters params;

  public OAuthHttpClient(OAuthClientParameters params){
    this.params = params;

    interceptors().add(new OauthAuthenticationHeaderInterceptor());
    interceptors().add(new StethoInterceptor());
  }

  private class OauthAuthenticationHeaderInterceptor implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {
      OkHttpLogger logger = new OkHttpLogger();
      logger.setPrintHeaders(true);

      Request request = chain.request();

      Request authenticatedRequest;
      String authorizationHeader;
      try {
        authorizationHeader =
            buildHeaderForParameters(request.method(), request.httpUrl().toString(), params);
      } catch (AuthorizationHeaderCreationException e) {
        throw new IOException(e);
      }
      authenticatedRequest = request.newBuilder()
          .header(HEADER_AUTHORIZATION, authorizationHeader)
          .method(request.method(), request.body())
          .build();

      logger.logRequest(TAG, authenticatedRequest);
      Response response = chain.proceed(authenticatedRequest);

      ResponseBody responseBody = response.body();
      String responseBodyString = response.body().string();
      Response newResponse = response.newBuilder().body(
          ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

      logger.logResponse(TAG, response);
      return newResponse;
    }

    private String buildHeaderForParameters(String method, String url, OAuthClientParameters params)
        throws AuthorizationHeaderCreationException {
      if(params.hasOauthToken()){
        return new AuthorizationHeader.Builder()
            .forRequestMethod(method)
            .forUrl(url)
            .withConsumerKeys(params.getConsumerKey(), params.getConsumerSecret())
            .withOauthToken(params.getOauthAccessToken(), params.getOauthAccessTokenSecret())
            .withExtraParameters(params.getExtraParameters())
            .build();
      }else{
        return new AuthorizationHeader.Builder()
            .forRequestMethod(method).forUrl(url)
            .withConsumerKeys(params.getConsumerKey(), params.getConsumerSecret())
            .withExtraParameters(params.getExtraParameters())
            .build();
      }
    }
  }
}