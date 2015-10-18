package net.byteabyte.beak.domain.post_update;

import net.byteabyte.beak.domain.Action;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.models.Tweet;

public class PostUpdateAction implements Action<PostUpdateInput, PostUpdateResponse, PostUpdateException> {

  private static final int MAX_TWEET_LENGTH = 140;
  private final PostUpdateClient client;
  private PostUpdateInput request;

  public PostUpdateAction(PostUpdateClient client){
    this.client = client;
  }

  @Override public void setRequestData(PostUpdateInput request) {
    this.request = request;
  }

  @Override public PostUpdateResponse call() throws PostUpdateException {
    if((request == null) || (client == null) || (StringUtils.isNullOrEmpty(request.getStatus()))) throw new PostUpdateException();

    if(request.getStatus().length() > MAX_TWEET_LENGTH){
      throw new PostUpdateException();
    }

    Tweet responseTweet = client.postUpdate(request);
    return new PostUpdateResponse(responseTweet);
  }
}