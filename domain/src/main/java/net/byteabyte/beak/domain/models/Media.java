package net.byteabyte.beak.domain.models;

public class Media {
  private final String url;
  private final String displayUrl;

  public Media(String url, String displayUrl) {
    this.url = url;
    this.displayUrl = displayUrl;
  }

  public String getMediaUrl() {
    return this.url;
  }

  public String getMediaDisplayUrl() {
    return this.displayUrl;
  }
}