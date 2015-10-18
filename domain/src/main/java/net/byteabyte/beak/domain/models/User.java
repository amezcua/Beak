package net.byteabyte.beak.domain.models;

public class User {
  private String name;
  private String screenName;
  private String location;
  private String description;
  private String url;
  private int followersCount;
  private int friendsCount;
  private boolean isVerified;
  private int statusesCount;
  private String profileImage;
  private boolean following;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getScreenName() {
    return this.screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getFollowersCount() {
    return this.followersCount;
  }

  public void setFollowersCount(int followersCount) {
    this.followersCount = followersCount;
  }

  public int getFriendsCount() {
    return this.friendsCount;
  }

  public void setFriendsCount(int friendsCount) {
    this.friendsCount = friendsCount;
  }

  public boolean isVerified() {
    return this.isVerified;
  }

  public void setIsVerified(boolean isVerified) {
    this.isVerified = isVerified;
  }

  public int getStatusesCount() {
    return this.statusesCount;
  }

  public void setStatusesCount(int statusesCount) {
    this.statusesCount = statusesCount;
  }

  public String getProfileImage() {
    return this.profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public boolean following() {
    return this.following;
  }

  public void setFollowing(boolean following) {
    this.following = following;
  }

  public String toString(){
    return "@" + this.screenName;
  }
}
