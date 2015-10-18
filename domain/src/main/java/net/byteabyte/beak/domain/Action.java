package net.byteabyte.beak.domain;

import java.util.concurrent.Callable;

public interface Action<I, O, E extends Exception> extends Callable<O> {

  void setRequestData(I request);

  @Override O call() throws E;
}