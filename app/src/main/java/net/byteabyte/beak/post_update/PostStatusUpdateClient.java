package net.byteabyte.beak.post_update;

import com.google.gson.GsonBuilder;
import net.byteabyte.beak.domain.Twitter;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.post_update.PostUpdateClient;
import net.byteabyte.beak.domain.post_update.PostUpdateException;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthHttpClient;
import net.byteabyte.beak.network.common.TwitterService;
import net.byteabyte.beak.network.models.twitter.NetworkMapper;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class PostStatusUpdateClient implements PostUpdateClient {
  @Override public Tweet postUpdate(PostUpdateInput input) throws PostUpdateException {
    OAuthClientParameters
        params = new OAuthClientParameters(input.getConsumerKey(), input.getConsumerSecret(), input.getOauthRequestToken(), input.getOauthSecret());

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat(Twitter.DATE_FORMAT);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.twitter.com")
        .client(new OAuthHttpClient(params))
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build();

    TwitterService service = retrofit.create(TwitterService.class);

    try {
      TwitterTweet serviceResponse = service.postStatusUpdate(input.getStatus()).execute().body();

      return mapResponses(serviceResponse);
    }catch (Exception e){
      throw new PostUpdateException(e);
    }
  }

  private Tweet mapResponses(TwitterTweet serviceResponse) {
    NetworkMapper mapper = new NetworkMapper();
    return mapper.mapTweet(serviceResponse);
  }
}
