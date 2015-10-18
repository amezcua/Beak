package net.byteabyte.beak.network.common;

import com.squareup.okhttp.ResponseBody;
import java.util.List;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface TwitterService {

  @POST("oauth/request_token") Call<ResponseBody> getLoginRequestToken();

  @FormUrlEncoded
  @POST("oauth/access_token") Call<ResponseBody> verifyLoginRequestToken(@Field("oauth_verifier") String oauthVerifier);

  @GET("/1.1/statuses/home_timeline.json") Call<List<TwitterTweet>> getHomeTimeline(@Query("max_id") String maxId);

  @POST("/1.1/statuses/update.json") Call<TwitterTweet> postStatusUpdate(@Query("status") String status);
}