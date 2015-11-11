package net.byteabyte.beak.presentation.post_update;

import net.byteabyte.beak.domain.post_update.PostUpdateAction;
import net.byteabyte.beak.domain.post_update.PostUpdateException;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.presentation.common.BackgroundTask;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.common.Presenter;

public class PostUpdatePresenter extends Presenter<PostUpdateView> {

  private final PostUpdateAction action;
  private final String consumerKey;
  private final String consumerSecret;
  private final String oauthToken;
  private final String oauthSecret;

  public PostUpdatePresenter(OutputThread outputThread, PostUpdateAction action, String consumerKey, String consumerSecret, String oauthToken, String oauthSecret) {
    super(outputThread);
    this.action = action;
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.oauthToken = oauthToken;
    this.oauthSecret = oauthSecret;
  }

  @BackgroundTask
  public void postStatusUpdate(final String statusUpdate) {
      try {
        PostUpdateInput input = new PostUpdateInput(consumerKey, consumerSecret, oauthToken, oauthSecret, statusUpdate);
        action.call(input);
        getView().onStatusPosted();
      } catch (PostUpdateException e) {
        getView().onStatusEmptyError();
      }
  }
}
