package net.byteabyte.beak.presentation.test;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineAction;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineClient;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineException;
import net.byteabyte.beak.domain.home_timeline.GetHomeTimelineInput;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.presentation.OutputThread;
import net.byteabyte.beak.presentation.main.MainPresenter;
import net.byteabyte.beak.presentation.main.MainView;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class HomeTimelinePresentation {

  MainPresenter presenter;
  List<Tweet> responseTweetList = new ArrayList<>();
  boolean clearCurrent = true;

  @Before public void setup(){
    presenter = new MainPresenter(new OutputThread() {
      @Override public void execute(Runnable runnable) {
        new Thread(runnable).start();
      }
    }, "test_key", "test_secret", new GetHomeTimelineAction(new GetHomeTimelineClient() {
      @Override public List<Tweet> getHomeTimeline(GetHomeTimelineInput input)
          throws GetHomeTimelineException {
        return responseTweetList;
      }
    }));
  }

  @Test public void displayTimelineCalledAfterTimelineIsRetrieved(){
    MainView view = mock(MainView.class);

    presenter.attachView(view);

    presenter.onLoginResult("test_token", "test_secret");
    presenter.onFirstPage();

    verify(view, timeout(1000)).displayTimeline(responseTweetList, clearCurrent);
  }

  @Test public void displayErrorIsCalledForNonLogeedInUsers(){
    MainView view = mock(MainView.class);

    presenter.attachView(view);

    presenter.onFirstPage();

    verify(view, timeout(1000)).displayTimelineLoadError();
  }
}
