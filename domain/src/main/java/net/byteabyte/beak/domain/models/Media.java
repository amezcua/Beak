package net.byteabyte.beak.domain.models;

public class Media {
  private String url;
  private String displayUrl;

  public String getMediaUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMediaDisplayUrl() {
    return this.displayUrl;
  }

  public void setDisplayUrl(String displayUrl) {
    this.displayUrl = displayUrl;
  }
}
