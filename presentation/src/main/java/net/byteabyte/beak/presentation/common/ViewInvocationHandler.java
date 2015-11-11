package net.byteabyte.beak.presentation.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewInvocationHandler implements InvocationHandler {

  private final Object wrappedView;
  private final OutputThread outputThread;

  public ViewInvocationHandler(Object wrappedView, OutputThread outputThread){
    this.wrappedView = wrappedView;
    this.outputThread = outputThread;
  }

  @Override public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
    outputThread.execute(new Runnable() {
      @Override public void run() {
        try {
          method.invoke(wrappedView, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    });
    return null;
  }
}