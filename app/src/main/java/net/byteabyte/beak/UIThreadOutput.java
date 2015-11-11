package net.byteabyte.beak;

import android.os.Handler;
import net.byteabyte.beak.presentation.common.OutputThread;

public class UIThreadOutput implements OutputThread {
  Handler handler = new Handler();

  @Override public void execute(Runnable runnable) {
    handler.post(runnable);
  }
}
