package net.byteabyte.beak.presentation.test;

import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.user_details.UserDetailsPresenter;
import net.byteabyte.beak.presentation.user_details.UserDetailsView;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PresenterTests {

  @Test public void detachedViewReturnsNullView(){

    UserDetailsView view = new UserDetailsView() {
      @Override public void displayUserDetails(User user) {}
    };

    UserDetailsPresenter presenter = new UserDetailsPresenter(new OutputThread() {
      @Override public void execute(Runnable runnable) {
        new Thread(runnable).start();
      }
    });

    presenter.attachView(view);
    presenter.detachView();

    assertTrue(presenter.getView() != null);
  }
}
