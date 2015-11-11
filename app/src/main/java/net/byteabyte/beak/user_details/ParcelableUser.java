package net.byteabyte.beak.user_details;

import android.os.Parcel;
import android.os.Parcelable;
import net.byteabyte.beak.domain.models.User;

public class ParcelableUser implements Parcelable {

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

  public ParcelableUser(User user){
    this.name = user.getName();
    this.screenName = user.getScreenName();
    this.location = user.getLocation();
    this.description = user.getDescription();
    this.url = user.getUrl();
    this.followersCount = user.getFollowersCount();
    this.friendsCount = user.getFriendsCount();
    this.isVerified = user.isVerified();
    this.statusesCount = user.getStatusesCount();
    this.profileImage = user.getProfileImage();
    this.following = user.following();
  }

  protected ParcelableUser(Parcel in) {
    name = in.readString();
    screenName = in.readString();
    location = in.readString();
    description = in.readString();
    url = in.readString();
    followersCount = in.readInt();
    friendsCount = in.readInt();
    isVerified = in.readByte() != 0x00;
    statusesCount = in.readInt();
    profileImage = in.readString();
    following = in.readByte() != 0x00;
  }

  public User createUser(){
    return new User(this.name, this.screenName, this.location, this.description, this.url, this.followersCount,
        this.friendsCount, this.isVerified, this.statusesCount, this.profileImage, this.following);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(screenName);
    dest.writeString(location);
    dest.writeString(description);
    dest.writeString(url);
    dest.writeInt(followersCount);
    dest.writeInt(friendsCount);
    dest.writeByte((byte) (isVerified ? 0x01 : 0x00));
    dest.writeInt(statusesCount);
    dest.writeString(profileImage);
    dest.writeByte((byte) (following ? 0x01 : 0x00));
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<ParcelableUser> CREATOR = new Parcelable.Creator<ParcelableUser>() {
    @Override
    public ParcelableUser createFromParcel(Parcel in) {
      return new ParcelableUser(in);
    }

    @Override
    public ParcelableUser[] newArray(int size) {
      return new ParcelableUser[size];
    }
  };
}
