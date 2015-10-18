package net.byteabyte.beak;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BeakActivityBase extends AppCompatActivity {

  private boolean homeAsBack = true;

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      if (isHomeAsBack()) {
        onBackPressed(); // Behave as a back button
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public boolean isHomeAsBack() {
    return homeAsBack;
  }

  public void setHomeAsBack(boolean homeAsBack) {
    this.homeAsBack = homeAsBack;
  }
}
