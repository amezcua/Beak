package net.byteabyte.beak.domain.models;

import java.util.Date;
import java.util.List;

public class Tweet{
  private final Date created;
  private final String id;
  private final List<Media> media;
  private final String text;
  private final User user;
  private final List<Url> urls;

  public Tweet(Date created, String id, List<Media> media, String text, User user, List<Url> urls){
    this.created = created;
    this.id = id;
    this.media = media;
    this.text = text;
    this.user = user;
    this.urls = urls;
  }

  public Date getCreated() {
    return this.created;
  }

  public String getId() {
    return id;
  }

  public List<Media> getMedia() {
    return this.media;
  }

  public String getText() {
    return this.text;
  }

  public List<Url> getUrls() {
    return this.urls;
  }

  public User getUser() {
    return this.user;
  }
}
