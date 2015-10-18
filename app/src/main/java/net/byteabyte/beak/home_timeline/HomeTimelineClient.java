package net.byteabyte.beak.home_timeline;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.Twitter;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineClient;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthHttpClient;
import net.byteabyte.beak.network.common.TwitterService;
import net.byteabyte.beak.network.models.twitter.NetworkMapper;
import net.byteabyte.beak.network.models.twitter.TweetTextUrlConverter;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class HomeTimelineClient implements GetHomeTimelineClient {
  @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
      throws GetHomeTimelineException {

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
      List<TwitterTweet> serviceResponse = service.getHomeTimeline(input.getMaxId()).execute().body();

      serviceResponse = filterUrls(serviceResponse);

      return mapResponses(serviceResponse);
    }catch (Exception e){
      throw new GetHomeTimelineException(e);
    }
  }

  private List<Tweet> mapResponses(List<TwitterTweet> serviceResponse) {
    ArrayList<Tweet> response = new ArrayList<>();
    NetworkMapper mapper = new NetworkMapper();

    for (TwitterTweet tt : serviceResponse) {
      response.add(mapper.mapTweet(tt));
    }
    return response;
  }

  private List<TwitterTweet> filterUrls(List<TwitterTweet> serviceResponse) {
    for(TwitterTweet tt: serviceResponse) {
      tt.text = new TweetTextUrlConverter().convertUrls(tt);
    }
    return serviceResponse;
  }
}