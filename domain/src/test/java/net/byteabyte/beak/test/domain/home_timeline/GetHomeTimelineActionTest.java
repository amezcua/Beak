package net.byteabyte.beak.test.domain.home_timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineAction;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineClient;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineResponse;
import net.byteabyte.beak.domain.models.Tweet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetHomeTimelineActionTest {

  @Test(expected = GetHomeTimelineException.class) public void actionWithNullInputThrows() throws GetHomeTimelineException {

    GetHomeTimelineAction action = new GetHomeTimelineAction(null);

    action.call();
  }

  @Test(expected = GetHomeTimelineException.class) public void actionWithInvalidInputThrows() throws GetHomeTimelineException {
    GetHomeTimelineInput input = new GetHomeTimelineInput(null, null, null, null, null);

    GetHomeTimelineAction action = new GetHomeTimelineAction(null);
    action.setRequestData(input);

    action.call();
  }

  @Test public void firstPageReturnsFullTweetList() throws GetHomeTimelineException {
    final List<Tweet> expectedResponse = new ArrayList<>();

    GetHomeTimelineInput input = new GetHomeTimelineInput("key", "secret", "token", "tokensecret", null);

    GetHomeTimelineClient client = new GetHomeTimelineClient() {
      @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
          throws GetHomeTimelineException {
        return expectedResponse;
      }
    };

    GetHomeTimelineAction action = new GetHomeTimelineAction(client);
    action.setRequestData(input);

    GetHomeTimelineResponse response = action.call();

    assertEquals(expectedResponse, response.getTimeline());
  }

  @Test public void followingPagesReturnEmptyListIfTwitterReturnsSingleItem()
      throws GetHomeTimelineException {
    final List<Tweet> twitterResponse = new ArrayList<>();
    twitterResponse.add(new Tweet(new Date(), "1234", null, "Test text", null, null));

    GetHomeTimelineInput input = new GetHomeTimelineInput("key", "secret", "token", "tokensecret", "1234");

    GetHomeTimelineClient client = new GetHomeTimelineClient() {
      @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
          throws GetHomeTimelineException {
        return twitterResponse;
      }
    };

    GetHomeTimelineAction action = new GetHomeTimelineAction(client);
    action.setRequestData(input);

    GetHomeTimelineResponse response = action.call();

    assertEquals(0, response.getTimeline().size());
  }

  @Test public void followingPagesExcludeMatchingMaxId() throws GetHomeTimelineException {
    final List<Tweet> twitterResponse = new ArrayList<>();
    twitterResponse.add(new Tweet(new Date(), "1234", null, "Test text 1234", null, null));
    twitterResponse.add(new Tweet(new Date(), "1235", null, "Test text 1235", null, null));

    GetHomeTimelineInput input = new GetHomeTimelineInput("key", "secret", "token", "tokensecret", "1234");

    GetHomeTimelineClient client = new GetHomeTimelineClient() {
      @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
          throws GetHomeTimelineException {
        return twitterResponse;
      }
    };

    GetHomeTimelineAction action = new GetHomeTimelineAction(client);
    action.setRequestData(input);

    GetHomeTimelineResponse response = action.call();

    assertEquals(1, response.getTimeline().size());
    assertEquals("1235", response.getTimeline().get(0).getId());
  }
}
