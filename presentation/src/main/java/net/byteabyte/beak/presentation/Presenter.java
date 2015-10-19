package net.byteabyte.beak.presentation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public abstract class Presenter<V> {

  private OutputThread outputThread;
  private V view;

  protected Presenter(OutputThread outputThread) {
    this.outputThread = outputThread;
  }

  public void attachView(V view) {
    this.view = view;
  }

  public void detachView(){
    view = createEmptyView(view);
  }

  public V getView() {
    return view;
  }

  protected void runOnBackgroundThread(Runnable runnable){
    new Thread(runnable).start();
  }

  public OutputThread getOutputThread() {
    return outputThread;
  }

  @SuppressWarnings("unchecked")
  private V createEmptyView(Object realObject) {
    Class<V> cls = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    return (V) Proxy.newProxyInstance(realObject.getClass().getClassLoader(), new Class[]{ cls }, new InvocationHandler() {
      @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
      }
    });
  }
}