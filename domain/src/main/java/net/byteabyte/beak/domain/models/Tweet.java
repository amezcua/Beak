package net.byteabyte.beak.domain.models;

import java.util.Date;
import java.util.List;

public class Tweet{
  private Date created;
  private String id;
  private List<Media> media;
  private String text;
  private User user;
  private List<Url> urls;

  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Media> getMedia() {
    return this.media;
  }

  public void setMedia(List<Media> media) {
    this.media = media;
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<Url> getUrls() {
    return this.urls;
  }

  public void setUrls(List<Url> urls) {
    this.urls = urls;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
