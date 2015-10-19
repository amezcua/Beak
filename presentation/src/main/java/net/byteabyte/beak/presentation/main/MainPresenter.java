package net.byteabyte.beak.presentation.main;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineAction;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineResponse;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.OutputThread;
import net.byteabyte.beak.presentation.Presenter;

public class MainPresenter extends Presenter<MainView> {

  private final String consumerKey;
  private final String consumerSecret;
  private final GetHomeTimelineAction getHomeTimelineAction;

  private List<Tweet> timeline = new ArrayList<>();

  private String oauthToken;
  private String oauthSecret;

  public MainPresenter(OutputThread outputThread, String consumerKey, String consumerSecret, GetHomeTimelineAction action) {
    super(outputThread);

    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;

    this.getHomeTimelineAction = action;
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

  private void loadHomeTimeline(final String maxId) {
    runOnBackgroundThread(new Runnable() {
      @Override public void run() {
        GetHomeTimelineInput input =
            new GetHomeTimelineInput(consumerKey, consumerSecret, oauthToken, oauthSecret, maxId);
        try {
          getHomeTimelineAction.setRequestData(input);
          GetHomeTimelineResponse response = getHomeTimelineAction.call();

          List<Tweet> newTimeline = response.getTimeline();

          timeline.addAll(newTimeline);
          getOutputThread().execute(new OnGetTimelineSuccess(timeline, maxId == null));
          getOutputThread().execute(new OnNewTimelineAvailable(newTimeline));
        } catch (GetHomeTimelineException e) {
          getOutputThread().execute(new OnGetTimelineError());
        }
      }
    });
  }

  public void onDisplayUserDetails(User user) {
    getView().navigateToUserDetails(user);
  }

  public void onPostUpdateClicked() {
    getView().navigateToPostStatusUpdate(oauthToken, oauthSecret);
  }

  private class OnGetTimelineSuccess implements Runnable{

    private final List<Tweet> timeline;
    private final boolean clearCurrent;

    public OnGetTimelineSuccess(List<Tweet> timeline, boolean clearCurrent){
      this.timeline = timeline;
      this.clearCurrent = clearCurrent;
    }

    @Override public void run() {
      getView().hideLogin();
      getView().hideLoading();
      getView().displayTimeline(timeline, clearCurrent);
    }
  }

  private class OnNewTimelineAvailable implements  Runnable{

    private final List<Tweet> timeline;

    public OnNewTimelineAvailable(List<Tweet> timeline){
      this.timeline = timeline;
    }

    @Override public void run() {
      getView().hideLoading();
      getView().displayNewTimelineInformation(this.timeline.size());
    }
  }

  private class OnGetTimelineError implements Runnable{
    @Override public void run() {
      getView().hideLoading();
      getView().displayTimelineLoadError();
    }
  }
}
