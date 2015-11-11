package net.byteabyte.beak.post_update;

import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.post_update.PostUpdateClient;
import net.byteabyte.beak.domain.post_update.PostUpdateException;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.network.common.OAuthClientParameters;
import net.byteabyte.beak.network.common.OAuthRetrofitClient;
import net.byteabyte.beak.network.common.TwitterService;
import net.byteabyte.beak.network.models.twitter.NetworkMapper;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;

public class PostUpdateClientImp extends OAuthRetrofitClient implements PostUpdateClient {
  @Override public Tweet postUpdate(PostUpdateInput input) throws PostUpdateException {

    try {
      TwitterTweet serviceResponse = buildTwitterService(input).
          postStatusUpdate(input.getStatus()).
          execute().body();

      return mapResponses(serviceResponse);
    }catch (Exception e){
      throw new PostUpdateException(e);
    }
  }

  private TwitterService buildTwitterService(PostUpdateInput input){
    OAuthClientParameters
        params = new OAuthClientParameters(input.getConsumerKey(), input.getConsumerSecret(), input.getOauthRequestToken(), input.getOauthSecret());

    return buildJsonOauthRetrofitClient(params).create(TwitterService.class);
  }

  private Tweet mapResponses(TwitterTweet serviceResponse) {
    NetworkMapper mapper = new NetworkMapper();
    return mapper.mapTweet(serviceResponse);
  }
}
