package net.byteabyte.beak.domain.models;

public class Url {
  private final String url;
  private final String displayUrl;

  public Url(String url, String displayUrl) {
    this.url = url;
    this.displayUrl = displayUrl;
  }

  public String getUrl() {
    return this.url;
  }

  public String getUrlDisplay() {
    return this.displayUrl;
  }
}
