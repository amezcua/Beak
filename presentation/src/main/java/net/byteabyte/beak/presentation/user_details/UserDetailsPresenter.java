package net.byteabyte.beak.presentation.user_details;

import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.common.Presenter;

public class UserDetailsPresenter extends Presenter<UserDetailsView>{

  private User user;

  public UserDetailsPresenter(OutputThread outputThread) {
    super(outputThread);
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void onResume() {
    getView().displayUserDetails(this.user);
  }
}
