package net.byteabyte.beak.network.models.twitter;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TwitterUser {

  public String name;
  public String screen_name;
  public String location;
  public String description;
  public String url;
  @SerializedName("protected")
  public boolean isProtected;
  public int followers_count;
  public int friends_count;
  public int listed_count;
  public Date created_at;
  public int favourites_count;
  public boolean verified;
  public int statuses_count;
  public String lang;
  public String profile_image_url;
  public boolean following;
}