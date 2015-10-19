package net.byteabyte.beak.presentation.test;

import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.OutputThread;
import net.byteabyte.beak.presentation.user_details.UserDetailsPresenter;
import net.byteabyte.beak.presentation.user_details.UserDetailsView;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class UserDetailsPresentation {

  User user;
  UserDetailsPresenter presenter;

  @Before public void setup(){

    user = new User();

    presenter = new UserDetailsPresenter(new OutputThread() {
      @Override public void execute(Runnable runnable) {
        new Thread(runnable).start();
      }
    });
  }

  @Test public void onResumeDisplayUserIsCalled(){

    UserDetailsView view = mock(UserDetailsView.class);

    presenter.attachView(view);

    presenter.setUser(user);
    presenter.onResume();

    verify(view, timeout(1000)).displayUserDetails(user);
  }
}
