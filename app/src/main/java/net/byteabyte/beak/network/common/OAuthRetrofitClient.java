package net.byteabyte.beak.network.common;

import com.google.gson.GsonBuilder;
import net.byteabyte.beak.domain.Twitter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public abstract class OAuthRetrofitClient {

  protected Retrofit buildOauthRetrofitClient(OAuthClientParameters params){
    return new Retrofit.Builder()
        .baseUrl("https://api.twitter.com")
        .client(new OAuthHttpClient(params))
        .build();
  }

  protected Retrofit buildJsonOauthRetrofitClient(OAuthClientParameters params){
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat(Twitter.DATE_FORMAT);

    return new Retrofit.Builder()
        .baseUrl("https://api.twitter.com")
        .client(new OAuthHttpClient(params))
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build();
  }
}
