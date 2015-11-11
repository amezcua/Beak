package net.byteabyte.beak.test.domain.post_update;

import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.post_update.PostUpdateAction;
import net.byteabyte.beak.domain.post_update.PostUpdateClient;
import net.byteabyte.beak.domain.post_update.PostUpdateException;
import net.byteabyte.beak.domain.post_update.PostUpdateInput;
import net.byteabyte.beak.domain.post_update.PostUpdateResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostUpdateActionTest {

  @Test(expected = PostUpdateException.class) public void postUpdateWithNullClientThrows() throws PostUpdateException{
    PostUpdateAction action = new PostUpdateAction(null);

    action.call(null);
  }

  @Test(expected = PostUpdateException.class) public void postUpdateWithoutStatusThrows()
      throws PostUpdateException {

    PostUpdateAction action = new PostUpdateAction(new PostUpdateClient() {
      @Override public Tweet postUpdate(PostUpdateInput input) {
        return null;
      }
    });

    action.call(null);
  }

  @Test(expected = PostUpdateException.class) public void postUpdateWithTooLongStatusThrows()
      throws PostUpdateException {

    PostUpdateAction action = new PostUpdateAction(new PostUpdateClient() {
      @Override public Tweet postUpdate(PostUpdateInput input) {
        return new PostUpdateResponse(null).getTweet();
      }
    });

    PostUpdateInput input = new PostUpdateInput(null, null, null, null, "This is the status which is too long for a tweet. "
        + "A tweet must be shorter that 140 characters. "
        + "So I will repeat. "
        + "This is the status which is too long for a tweet. A tweet must be shorter that 140 characters.");

    action.call(input);
  }

  @Test public void postUpdateWithValidStatusReturnsTweet() throws PostUpdateException {

    final String status = "This is the tweet";
    final PostUpdateInput input = new PostUpdateInput(null, null, null, null, status);

    PostUpdateAction action = new PostUpdateAction(new PostUpdateClient() {
      @Override public Tweet postUpdate(PostUpdateInput input1) {
        Tweet createdTweet = new Tweet(null, null, null, input.getStatus(), null, null);
        PostUpdateResponse response = new PostUpdateResponse(createdTweet);
        return response.getTweet();
      }
    });

    PostUpdateResponse response = action.call(input);

    assertEquals(status, response.getTweet().getText());
  }
}
