package net.byteabyte.beak.user_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.byteabyte.beak.BeakActivityBase;
import net.byteabyte.beak.R;
import net.byteabyte.beak.UIThreadOutput;
import net.byteabyte.beak.domain.StringUtils;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.user_details.UserDetailsPresenter;
import net.byteabyte.beak.presentation.user_details.UserDetailsView;

public class UserDetailsActivity extends BeakActivityBase implements UserDetailsView {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.user_image) ImageView userImage;
  @Bind(R.id.user_name) TextView userName;
  @Bind(R.id.user_display_name) TextView userScreenName;
  @Bind(R.id.user_description) TextView userDescription;
  @Bind(R.id.user_followers) TextView userFollowers;
  @Bind(R.id.user_statuses_count) TextView userStatusesCount;
  @Bind(R.id.user_location) TextView userLocation;

  public static final int REQUEST_CODE = 8748;
  public static final String USER_PARCELABLE = "user_parcelable";

  private UserDetailsPresenter presenter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    presenter = new UserDetailsPresenter(new UIThreadOutput());

    if(getIntent().hasExtra(USER_PARCELABLE)){
      ParcelableUser parcelableUser = getIntent().getExtras().getParcelable(USER_PARCELABLE);
      presenter.setUser(parcelableUser.createUser());
    }

    setContentView(R.layout.activity_user_details);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override protected void onResume() {
    super.onResume();

    presenter.attachView(this);
    presenter.onResume();
  }

  @Override protected void onDestroy() {
    presenter.detachView();
    super.onDestroy();
  }

  @Override public void displayUserDetails(User user) {

    Glide.with(this)
        .load(user.getProfileImage())
        .centerCrop()
        .placeholder(R.drawable.user_placeholder)
        .crossFade()
        .into(userImage);

    userName.setText(user.getName());
    userScreenName.setText("@" + user.getScreenName());
    displayDescription(user);
    displayFollowersCount(user);
    displayStatusesCount(user);
    displayLocation(user);
  }

  private void displayDescription(User user) {
    String description = user.getDescription();
    if(StringUtils.isNullOrEmpty(description)){
      userDescription.setVisibility(View.GONE);
    }else {
      userDescription.setVisibility(View.VISIBLE);
      userDescription.setText(description);
    }
  }

  private void displayFollowersCount(User user) {
    String followersCount = getFormatter().format(user.getFollowersCount()) + " " + getString(R.string.followers);

    userFollowers.setText(followersCount);
  }

  private void displayStatusesCount(User user) {
    String followersCount = getFormatter().format(user.getStatusesCount()) + " " + getString(R.string.tweets);

    userStatusesCount.setText(followersCount);
  }

  private void displayLocation(User user) {
    String location = user.getLocation();

    if(StringUtils.isNullOrEmpty(location)){
      userLocation.setVisibility(View.GONE);
    }else{
      userLocation.setVisibility(View.VISIBLE);
      userLocation.setText(getString(R.string.from) + " " + location);
    }
  }

  @NonNull private DecimalFormat getFormatter() {
    return (DecimalFormat) NumberFormat.getInstance();
  }
}
