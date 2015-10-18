package net.byteabyte.beak.network.models.twitter;

import java.util.Date;

public class TwitterTweet {
  public Date created_at;
  public String id_str;
  public String text;
  public TwitterUser user;
  public boolean truncated;
  public String in_reply_to_status_id_str;
  public String in_reply_to_user_id_str;
  public String in_reply_to_screen_name;
  public boolean is_quote_status;
  public int retweet_count;
  public int favorite_count;
  public TwitterEntities entities;
  public boolean favorited;
  public boolean retweeted;
  public boolean possibly_sensitive;
  public String lang;
}
