package net.byteabyte.beak.presentation;

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
    view = null;
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
}