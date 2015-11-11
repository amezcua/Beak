package net.byteabyte.beak.presentation.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public abstract class Presenter<V> {

  private OutputThread outputThread;
  private V view;
  private ViewInvocationHandler viewInvocationhandler = null;

  protected Presenter(OutputThread outputThread) {
    this.outputThread = outputThread;
  }

  public void attachView(V view) {
    this.view = createProxyView(view);
  }

  public void detachView(){
    view = createEmptyView(view);
  }

  public V getView() {
    return view;
  }


  @SuppressWarnings("unchecked")
  private V createProxyView(final Object realObject) {

    Class<V> cls = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    return (V) Proxy.newProxyInstance(realObject.getClass().getClassLoader(), new Class[] { cls },
        getViewInvocationhandler(realObject, outputThread));
  }

  private ViewInvocationHandler getViewInvocationhandler(Object view, OutputThread outputThread){
    if(viewInvocationhandler == null){
      viewInvocationhandler = new ViewInvocationHandler(view, outputThread);
    }

    return viewInvocationhandler;
  }

  @SuppressWarnings("unchecked")
  private V createEmptyView(Object realObject) {
    Class<V> cls = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    return (V) Proxy.newProxyInstance(realObject.getClass().getClassLoader(), new Class[] { cls },
        new InvocationHandler() {
          @Override public Object invoke(Object proxy, Method method, Object[] args)
              throws Throwable {
            return null;
          }
        });
  }
}