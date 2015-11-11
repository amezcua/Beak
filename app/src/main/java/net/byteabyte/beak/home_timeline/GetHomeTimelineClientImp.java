package net.byteabyte.beak.home_timeline;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineClient;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthRetrofitClient;
import net.byteabyte.beak.network.common.TwitterService;
import net.byteabyte.beak.network.models.twitter.NetworkMapper;
import net.byteabyte.beak.network.models.twitter.TweetTextUrlConverter;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;

public class GetHomeTimelineClientImp extends OAuthRetrofitClient implements GetHomeTimelineClient {
  @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
      throws GetHomeTimelineException {

    try {
      List<TwitterTweet> twitterResponse = buildTwitterService(input).
          getHomeTimeline(input.getMaxId()).execute().body();

      return mapResponses(filterUrls(twitterResponse));
    }catch (Exception e){
      throw new GetHomeTimelineException(e);
    }
  }

  private TwitterService buildTwitterService(GetHomeTimelineInput input){
    OAuthClientParameters params = new OAuthClientParameters(input.getConsumerKey(),
        input.getConsumerSecret(), input.getOauthRequestToken(), input.getOauthSecret());

    return buildJsonOauthRetrofitClient(params).create(TwitterService.class);
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