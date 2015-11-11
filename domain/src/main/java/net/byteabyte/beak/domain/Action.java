package net.byteabyte.beak.domain;

public interface Action<I, O, E extends Exception> {
  O call(I request) throws E;
}