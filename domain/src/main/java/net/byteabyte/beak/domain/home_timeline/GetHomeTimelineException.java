package net.byteabyte.beak.domain.home_timeline;

public class GetHomeTimelineException extends Exception {
  public GetHomeTimelineException(){}

  public GetHomeTimelineException(Exception reason){
    super(reason);
  }

  public GetHomeTimelineException(String s) {
    super(s);
  }
}
