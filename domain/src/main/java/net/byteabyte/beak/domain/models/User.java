package net.byteabyte.beak.domain.models;

public class User {
  private final String name;
  private final String screenName;
  private final String location;
  private final String description;
  private final String url;
  private final int followersCount;
  private final int friendsCount;
  private final boolean isVerified;
  private final int statusesCount;
  private final String profileImage;
  private final boolean following;

  public User(String name, String screenName, String location, String description, String url,
      int followersCount, int friendsCount, boolean isVerified, int statusesCount,
      String profileImage, boolean following) {
    this.name = name;
    this.screenName = screenName;
    this.location = location;
    this.description = description;
    this.url = url;
    this.followersCount = followersCount;
    this.friendsCount = friendsCount;
    this.isVerified = isVerified;
    this.statusesCount = statusesCount;
    this.profileImage = profileImage;
    this.following = following;
  }

  public String getName() {
    return this.name;
  }

  public String getScreenName() {
    return this.screenName;
  }

  public String getLocation() {
    return this.location;
  }

  public String getDescription() {
    return this.description;
  }

  public String getUrl() {
    return this.url;
  }

  public int getFollowersCount() {
    return this.followersCount;
  }

  public int getFriendsCount() {
    return this.friendsCount;
  }

  public boolean isVerified() {
    return this.isVerified;
  }

  public int getStatusesCount() {
    return this.statusesCount;
  }

  public String getProfileImage() {
    return this.profileImage;
  }

  public boolean following() {
    return this.following;
  }

  public String toString(){
    return "@" + this.screenName;
  }
}
