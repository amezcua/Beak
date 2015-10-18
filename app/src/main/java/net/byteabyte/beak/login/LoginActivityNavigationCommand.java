package net.byteabyte.beak.login;

import android.app.Activity;
import android.content.Intent;
import net.byteabyte.beak.presentation.NavigationCommand;

public class LoginActivityNavigationCommand implements NavigationCommand {

  private final Activity fromActivity;

  public LoginActivityNavigationCommand(Activity from){
    this.fromActivity = from;
  }

  @Override public void navigate() {
    Intent startIntent = new Intent(fromActivity, LoginActivity.class);
    fromActivity.startActivityForResult(startIntent, LoginActivity.LOGIN_REQUEST_CODE);
  }
}
