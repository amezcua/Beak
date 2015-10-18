package net.byteabyte.beak.domain.home_timeline;

import java.util.List;
import net.byteabyte.beak.domain.models.Tweet;

public interface GetHomeTimelineClient {
  List<Tweet> getHomeTimeline(GetHomeTimelineInput input) throws GetHomeTimelineException;
}
