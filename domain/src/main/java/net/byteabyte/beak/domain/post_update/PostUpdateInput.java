package net.byteabyte.beak.domain.post_update;

import net.byteabyte.beak.domain.OAuthClientInput;

public class PostUpdateInput extends OAuthClientInput {

  private final String status;

  public PostUpdateInput(String consumerKey, String consumerSecret, String oauthRequestToken, String oauthSecret, String status) {
    super(consumerKey, consumerSecret, oauthRequestToken, oauthSecret);

    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
