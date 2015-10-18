package net.byteabyte.beak.domain.home_timeline;

import net.byteabyte.beak.domain.OAuthClientInput;

public class GetHomeTimelineInput extends OAuthClientInput {

  private final String maxId;

  public GetHomeTimelineInput(String consumerKey, String consumerSecret,
      String oauthRequestToken, String oauthSecret, String maxId) {
    super(consumerKey, consumerSecret, oauthRequestToken, oauthSecret);
    this.maxId = maxId;
  }

  public String getMaxId() {
    return maxId;
  }
}
