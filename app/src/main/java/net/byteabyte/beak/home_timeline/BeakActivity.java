package net.byteabyte.beak.home_timeline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;
import net.byteabyte.beak.BuildConfig;
import net.byteabyte.beak.R;
import net.byteabyte.beak.UIThreadOutput;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import net.byteabyte.beak.login.LoginActivity;
import net.byteabyte.beak.login.LoginActivityNavigationCommand;
import net.byteabyte.beak.post_update.PostUpdateActivity;
import net.byteabyte.beak.post_update.PostUpdateNavigationCommand;
import net.byteabyte.beak.presentation.main.MainPresenter;
import net.byteabyte.beak.presentation.main.MainView;
import net.byteabyte.beak.user_details.UserDetailsActivityNavigationCommand;

public class BeakActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.loginWrapper) View loginWrapper;
  @Bind(R.id.timelineWrapper) View timelineWrapper;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.loginButton) Button loginButton;
  @Bind(R.id.timelineRefresh) SwipeRefreshLayout timelineRefresh;
  @Bind(R.id.tweetsList) RecyclerView tweetList;
  @Bind(R.id.post_tweet_button) View postTweetButton;

  Pair<View, String>[] pairedViews;

  private HomeTimelineAdapter timelineAdapter;
  private LinearLayoutManager layoutManager;

  private MainPresenter presenter;
  private static final String TIMELINE_STATE_KEY = "timelinestate";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    presenter = new MainPresenter(new UIThreadOutput(), new GetHomeTimelineClientImp(), BuildConfig.twitterConsumerKey, BuildConfig.twitterConsumerSecret);

    setContentView(R.layout.activity_beak);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    tweetList.addOnScrollListener(listScrollListener);

    timelineRefresh.setOnRefreshListener(this);

    tweetList.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);

    tweetList.setLayoutManager(layoutManager);

    timelineAdapter = new HomeTimelineAdapter(this, new TweetTimelineListener(){
      @Override public void onDisplayUserDetails(TweetViewHolder tweetViewHolder, User user) {
        pairedViews = buildPairedElements(tweetViewHolder);
        presenter.onDisplayUserDetails(user);
      }
    });


    postTweetButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.onPostUpdateClicked();
      }
    });

    tweetList.setAdapter(timelineAdapter);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putParcelable(TIMELINE_STATE_KEY, tweetList.getLayoutManager().onSaveInstanceState());
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    if(savedInstanceState != null) {
      Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(TIMELINE_STATE_KEY);
      tweetList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == RESULT_OK){
      if(data.hasExtra(OauthKeys.OAUTH_TOKEN.getKey())){
        String token = data.getStringExtra(OauthKeys.OAUTH_TOKEN.getKey());
        String secret = data.getStringExtra(OauthKeys.OAUTH_TOKEN_SECRET.getKey());
        presenter.onLoginResult(token, secret);
      }else{
        presenter.onLoginResultWithoutOauthData();
      }
    }else if(requestCode == PostUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK){
      presenter.onRefresh();
    }else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override protected void onResume() {
    super.onResume();

    presenter.attachView(this);

    presenter.onResume();

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.onLoginClicked();
      }
    });
  }

  @SuppressWarnings("unchecked")
  private Pair<View, String>[] buildPairedElements(TweetViewHolder tweetViewHolder) {
    return new Pair[] {
        Pair.create(toolbar, getString(R.string.transition_toolbar)),
        Pair.create(tweetViewHolder.userImage, getString(R.string.transition_user_image)),
        Pair.create(tweetViewHolder.userName, getString(R.string.transition_user_name)),
        Pair.create(tweetViewHolder.userDisplayName,
            getString(R.string.transition_user_screen_name))
    };
  }

  @Override public void navigateToUserDetails(User user) {
    new UserDetailsActivityNavigationCommand(this, user)
        .withPairedElements(pairedViews)
        .navigate();
  }

  @Override public void navigateToPostStatusUpdate(String oauthToken, String oauthSecret){
    new PostUpdateNavigationCommand(BeakActivity.this, oauthToken, oauthSecret).navigate();
  }

  @Override public void hidePostStatusUpdateButton() {
    postTweetButton.setVisibility(View.GONE);
  }

  @Override public void showPostStatusUpdateButton() {
    postTweetButton.setVisibility(View.VISIBLE);
  }

  @Override protected void onDestroy() {
    presenter.detachView();
    super.onDestroy();
  }

  @Override public void showLogin() {
    new LoginActivityNavigationCommand(this).navigate();
  }

  @Override public void showResultLoginError() {
    Snackbar.make(loginButton, R.string.login_error, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void showAnonymousView() {
    Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
  }

  @Override public void displayTimeline(List<Tweet> timeline, boolean clearCurrent) {
    timelineWrapper.setVisibility(View.VISIBLE);
    if(clearCurrent){
      timelineAdapter.setTimeline(timeline);
      timelineAdapter.notifyDataSetChanged();
    }else{
      int currentCount = timelineAdapter.getItemCount();
      timelineAdapter.setTimeline(timeline);
      timelineAdapter.notifyItemInserted(currentCount);
    }
  }

  @Override public void displayNewTimelineInformation(int tweetCount) {
    String tweetCountMessage = tweetCount + " " + getString(R.string.new_tweets);
    Snackbar.make(loginButton, tweetCountMessage, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void displayTimelineLoadError() {
    Toast.makeText(this, "Error loading the timeline. Please try again.", Toast.LENGTH_SHORT).show();
  }

  @Override public void hideLogin() {
    loginWrapper.setVisibility(View.GONE);
  }

  @Override public void onRefresh() {
    presenter.onRefresh();
  }

  @Override public void showLoading() {
    timelineRefresh.setRefreshing(true);
  }

  @Override public void hideLoading() {
    timelineRefresh.setRefreshing(false);
  }

  private final RecyclerView.OnScrollListener listScrollListener = new RecyclerView.OnScrollListener() {
    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
      if(newState == RecyclerView.SCROLL_STATE_IDLE) {
        if (layoutManager.findLastVisibleItemPosition() == timelineAdapter.getItemCount() - 1) {
          presenter.onNextPage();
        }
      }
    }
  };
}
