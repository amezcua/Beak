package net.byteabyte.beak.presentation.post_update;

import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.post_update.PostUpdateAction;
import net.byteabyte.beak.domain.post_update.PostUpdateException;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.domain.post_update.PostUpdateResponse;
import net.byteabyte.beak.presentation.OutputThread;
import net.byteabyte.beak.presentation.Presenter;

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

  public void postStatusUpdate(final String statusUpdate) {

    runOnBackgroundThread(new Runnable() {
      @Override public void run() {
        try {
          PostUpdateInput input = new PostUpdateInput(consumerKey, consumerSecret, oauthToken, oauthSecret, statusUpdate);
          action.setRequestData(input);
          PostUpdateResponse response = action.call();
          getOutputThread().execute(new OnPostUpdateSuccess(response.getTweet()));
        } catch (PostUpdateException e) {
          getOutputThread().execute(new OnPostUpdateError());
        }
      }
    });
  }

  private class OnPostUpdateSuccess implements Runnable{
    private final Tweet tweet;

    public OnPostUpdateSuccess(Tweet tweet){
      this.tweet = tweet;
    }

    @Override public void run() {
      if(getView() == null) return;

      getView().onStatusPosted();
    }
  }

  private class OnPostUpdateError implements Runnable{
    @Override public void run() {
      if(getView() == null) return;

      getView().onStatusEmptyError();
    }
  }
}
