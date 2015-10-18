package net.byteabyte.beak.domain.post_update;

public class PostUpdateException extends Exception {
  public PostUpdateException(){}

  public PostUpdateException(Exception source){
    super(source);
  }
}
