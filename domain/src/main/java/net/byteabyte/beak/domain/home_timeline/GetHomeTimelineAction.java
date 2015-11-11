package net.byteabyte.beak.domain.home_timeline;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.Action;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.models.Tweet;

public class GetHomeTimelineAction implements
    Action<GetHomeTimelineInput, GetHomeTimelineResponse, GetHomeTimelineException> {

  private GetHomeTimelineClient client;

  public GetHomeTimelineAction(GetHomeTimelineClient client) {
    this.client = client;
  }

  @Override public GetHomeTimelineResponse call(GetHomeTimelineInput request) throws GetHomeTimelineException {
    if(request == null || StringUtils.isNullOrEmpty(request.getOauthRequestToken())) throw  new GetHomeTimelineException("The input data can not be empty");

    List<Tweet> getHomeTimelineResponse = client.getHomeTimeline(request);

    if(request.getMaxId() == null){
      return new GetHomeTimelineResponse(getHomeTimelineResponse);
    }else if(getHomeTimelineResponse.size() <= 1) {
      return new GetHomeTimelineResponse(new ArrayList<Tweet>());
    }else{
      return new GetHomeTimelineResponse(getHomeTimelineResponse.subList(1, getHomeTimelineResponse.size()));
    }
  }
}
