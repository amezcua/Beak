package net.byteabyte.beak.user_details;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.NavigationCommand;

public class UserDetailsActivityNavigationCommand implements NavigationCommand {

  private final AppCompatActivity fromActivity;
  private final User user;
  private Pair<View, String>[] sharedViews;

  public UserDetailsActivityNavigationCommand(AppCompatActivity fromActivity, User user){
    this.fromActivity = fromActivity;
    this.user = user;
  }

  public UserDetailsActivityNavigationCommand withPairedElements(Pair<View, String>[] pairedViews) {
    this.sharedViews = pairedViews;
    return this;
  }

  @Override public void navigate() {
    Intent startIntent = new Intent(fromActivity, UserDetailsActivity.class);
    startIntent.putExtra(UserDetailsActivity.USER_PARCELABLE, new ParcelableUser(this.user));

    if(sharedViews != null) {
      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fromActivity, sharedViews);

      fromActivity.startActivityForResult(startIntent, UserDetailsActivity.REQUEST_CODE, options.toBundle());
    }else{
      fromActivity.startActivityForResult(startIntent, UserDetailsActivity.REQUEST_CODE);
    }
  }
}
