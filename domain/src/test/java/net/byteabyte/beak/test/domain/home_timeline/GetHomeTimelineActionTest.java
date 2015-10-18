package net.byteabyte.beak.test.domain.home_timeline;

import java.util.ArrayList;
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

  @Test public void actionReturnsValidResponse() throws GetHomeTimelineException {
    final List<Tweet> expectedResponse = new ArrayList<>();

    GetHomeTimelineInput input = new GetHomeTimelineInput("key", "secret", "token", "tokensecret", "0");

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
}
