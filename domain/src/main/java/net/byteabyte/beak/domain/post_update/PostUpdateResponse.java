package net.byteabyte.beak.domain.post_update;

import net.byteabyte.beak.domain.models.Tweet;

public class PostUpdateResponse {

  private final Tweet recentStatus;

  public PostUpdateResponse(Tweet recentStatus){
    this.recentStatus = recentStatus;
  }

  public Tweet getTweet() {
    return recentStatus;
  }
}
