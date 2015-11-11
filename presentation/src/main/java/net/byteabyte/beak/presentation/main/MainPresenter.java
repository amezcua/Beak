package net.byteabyte.beak.presentation.main;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineAction;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineClient;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineResponse;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.common.BackgroundTask;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.common.Presenter;

public class MainPresenter extends Presenter<MainView> {

  private final GetHomeTimelineClient homeTimelineClient;
  private final String consumerKey;
  private final String consumerSecret;

  private List<Tweet> timeline = new ArrayList<>();

  private String oauthToken;
  private String oauthSecret;

  public MainPresenter(OutputThread outputThread, GetHomeTimelineClient homeTimelineClient,
      String twitterConsumerKey, String twitterConsumerSecret) {
    super(outputThread);

    this.homeTimelineClient = homeTimelineClient;
    this.consumerKey = twitterConsumerKey;
    this.consumerSecret = twitterConsumerSecret;
  }

  public void onLoginClicked() {
    getView().showLogin();
  }

  public void onLoginResult(String token, String secret) {
    this.oauthToken = token;
    this.oauthSecret = secret;
  }

  public void onLoginResultWithoutOauthData() {
    getView().showResultLoginError();
  }

  public void onResume() {
    if(StringUtils.isNullOrEmpty(oauthToken)){
      getView().showAnonymousView();
      getView().hidePostStatusUpdateButton();
    }else{
      if(this.timeline.size() == 0) {
        onFirstPage();
        getView().showPostStatusUpdateButton();
      }
    }
  }

  public void onRefresh() {
    this.timeline.clear();
    onFirstPage();
  }

  public void onFirstPage() {
    getView().showLoading();

    loadHomeTimeline(null);
  }

  public void onNextPage() {
    getView().showLoading();

    loadHomeTimeline(getCurrentMaxId(this.timeline));
  }

  private String getCurrentMaxId(List<Tweet> timeline) {
    if(timeline.size() == 0){
      return null;
    }else {
      Tweet lastTweet = timeline.get(timeline.size() - 1);
      return lastTweet.getId();
    }
  }

  @BackgroundTask
  private void loadHomeTimeline(final String maxId) {
    try {
      GetHomeTimelineAction getHomeTimelineAction = new GetHomeTimelineAction(homeTimelineClient);

      GetHomeTimelineResponse response = getHomeTimelineAction.call(new GetHomeTimelineInput(consumerKey, consumerSecret, oauthToken, oauthSecret, maxId));

      List<Tweet> newTimeline = response.getTimeline();

      timeline.addAll(newTimeline);
      onTimelineSuccess(maxId, timeline, newTimeline);
    } catch (GetHomeTimelineException e) {
      onTimelineError();
    }
  }

  public void onTimelineSuccess(String maxId, List<Tweet> timeline, List<Tweet> newTimeline){
    getView().hideLogin();
    getView().hideLoading();
    getView().displayTimeline(timeline, maxId == null);

    getView().displayNewTimelineInformation(newTimeline.size());
  }

  public void onTimelineError(){
    getView().hideLoading();
    getView().displayTimelineLoadError();
  }


  public void onDisplayUserDetails(User user) {
    getView().navigateToUserDetails(user);
  }

  public void onPostUpdateClicked() {
    getView().navigateToPostStatusUpdate(oauthToken, oauthSecret);
  }
}