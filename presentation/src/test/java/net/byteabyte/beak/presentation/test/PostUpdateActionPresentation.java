package net.byteabyte.beak.presentation.test;

import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.post_update.PostUpdateAction;
import net.byteabyte.beak.domain.post_update.PostUpdateClient;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.domain.post_update.PostUpdateResponse;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.post_update.PostUpdatePresenter;
import net.byteabyte.beak.presentation.post_update.PostUpdateView;
import org.junit.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class PostUpdateActionPresentation {

  PostUpdatePresenter presenter;

  @Before public void setupPresenter(){
    presenter = new PostUpdatePresenter(new OutputThread() {
      @Override public void execute(Runnable runnable) {
        new Thread(runnable).run();
      }
    }, new PostUpdateAction(new PostUpdateClient() {
      @Override public Tweet postUpdate(PostUpdateInput input) {
        Tweet createdTweet = new Tweet(null, null, null, input.getStatus(), null, null);
        PostUpdateResponse response = new PostUpdateResponse(createdTweet);
        return response.getTweet();
      }
    }), null, null, null, null);
  }

  @Test public void postWithoutStatusCallsError(){

    PostUpdateView view = mock(PostUpdateView.class);

    presenter.attachView(view);

    presenter.postStatusUpdate(null);

    verify(view, timeout(1000)).onStatusEmptyError();
  }

  @Test public void postWithLongStatusUpdateCallsError(){
    PostUpdateView view = mock(PostUpdateView.class);

    presenter.attachView(view);

    String update = "This is the status which is too long for a tweet. "
        + "A tweet must be shorter that 140 characters. "
        + "So I will repeat. "
        + "This is the status which is too long for a tweet. A tweet must be shorter that 140 characters.";

    presenter.postStatusUpdate(update);

    verify(view, timeout(1000)).onStatusEmptyError();
  }

  @Test public void postWithValidStatusCallsSucces(){
    PostUpdateView view = mock(PostUpdateView.class);

    presenter.attachView(view);

    String update = "This is the status which is not too long for a tweet.";

    presenter.postStatusUpdate(update);

    verify(view, timeout(1000)).onStatusPosted();
  }
}
