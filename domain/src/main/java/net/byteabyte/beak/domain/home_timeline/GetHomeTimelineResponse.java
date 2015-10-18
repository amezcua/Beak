package net.byteabyte.beak.domain.home_timeline;

import java.util.List;
import net.byteabyte.beak.domain.models.Tweet;

public class GetHomeTimelineResponse {

  private final List<Tweet> timeline;

  public GetHomeTimelineResponse(List<Tweet> timeline) {
    this.timeline = timeline;
  }

  public List<Tweet> getTimeline() {
    return timeline;
  }
}
