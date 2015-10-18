package net.byteabyte.beak.presentation.main;

import java.util.List;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.User;

public interface MainView {
  void displayNewTimelineInformation(int tweetCount);

  void displayTimeline(List<Tweet> timeline, boolean clearCurrent);

  void displayTimelineLoadError();

  void hideLoading();

  void hideLogin();

  void hidePostStatusUpdateButton();

  void navigateToPostStatusUpdate(String oauthToken, String oauthSecret);

  void navigateToUserDetails(User user);

  void showAnonymousView();

  void showLoading();

  void showLogin();

  void showPostStatusUpdateButton();

  void showResultLoginError();
}
