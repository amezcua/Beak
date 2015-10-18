package net.byteabyte.beak.post_update;

import android.content.Intent;
import net.byteabyte.beak.home_timeline.BeakActivity;
import net.byteabyte.beak.presentation.NavigationCommand;

public class PostUpdateNavigationCommand implements NavigationCommand {

  private final BeakActivity fromActivity;
  private final String oauthToken;
  private final String oauthSecret;

  public PostUpdateNavigationCommand(BeakActivity fromActivity, String oauthToken, String oauthSecret){
    this.fromActivity = fromActivity;
    this.oauthToken = oauthToken;
    this.oauthSecret = oauthSecret;
  }

  @Override public void navigate() {
    Intent startIntent = new Intent(fromActivity, PostUpdateActivity.class);

    startIntent.putExtra(PostUpdateActivity.EXTRA_OAUTH_TOKEN, oauthToken);
    startIntent.putExtra(PostUpdateActivity.EXTRA_OAUTH_SECRET, oauthSecret);

    fromActivity.startActivityForResult(startIntent, PostUpdateActivity.REQUEST_CODE);
  }
}
