package net.byteabyte.beak.domain.home_timeline;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.Action;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.models.Tweet;

public class GetHomeTimelineAction implements
    Action<GetHomeTimelineInput, GetHomeTimelineResponse, GetHomeTimelineException> {

  private GetHomeTimelineClient client;
  private GetHomeTimelineInput input;

  public GetHomeTimelineAction(GetHomeTimelineClient client) {
    this.client = client;
  }

  @Override public void setRequestData(GetHomeTimelineInput request) {
    this.input = request;
  }

  @Override public GetHomeTimelineResponse call() throws GetHomeTimelineException {
    if(this.input == null || StringUtils.isNullOrEmpty(this.input.getOauthRequestToken())) throw  new GetHomeTimelineException("The input data can not be empty");

    List<Tweet> getHomeTimelineResponse = client.getHomeTimeline(this.input);

    if(input.getMaxId() == null){
      return new GetHomeTimelineResponse(getHomeTimelineResponse);
    }else if(getHomeTimelineResponse.size() <= 1) {
      return new GetHomeTimelineResponse(new ArrayList<Tweet>());
    }else{
      return new GetHomeTimelineResponse(getHomeTimelineResponse.subList(1, getHomeTimelineResponse.size()));
    }
  }
}
