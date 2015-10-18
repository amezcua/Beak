package net.byteabyte.beak.network.models.twitter;

import java.util.ArrayList;
import java.util.List;
import net.byteabyte.beak.domain.models.Media;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.Url;
import net.byteabyte.beak.domain.models.User;

public class NetworkMapper {

  public Tweet mapTweet(TwitterTweet networkTweet){
    if(networkTweet == null) return null;

    Tweet tweet = new Tweet();
    tweet.setId(networkTweet.id_str);
    tweet.setCreated(networkTweet.created_at);
    tweet.setText(networkTweet.text);

    tweet.setUser(mapUser(networkTweet.user));
    tweet.setUrls(mapUrls(networkTweet.entities.urls));
    tweet.setMedia(mapMedia(networkTweet.entities.media));

    return tweet;
  }

  private User mapUser(TwitterUser twitterUser) {
    if(twitterUser == null) return null;

    User user = new User();

    user.setDescription(twitterUser.description);
    user.setFollowersCount(twitterUser.followers_count);
    user.setFollowing(twitterUser.following);
    user.setFriendsCount(twitterUser.friends_count);
    user.setIsVerified(twitterUser.verified);
    user.setLocation(twitterUser.location);
    user.setName(twitterUser.name);
    user.setProfileImage(twitterUser.profile_image_url);
    user.setScreenName(twitterUser.screen_name);
    user.setUrl(twitterUser.url);
    user.setStatusesCount(twitterUser.statuses_count);

    return user;
  }

  private List<Url> mapUrls(List<TwitterUrl> urls) {
    if(urls == null) return null;

    ArrayList<Url> mappedUrls = new ArrayList<>();

    for(TwitterUrl url: urls){
      Url beakUrl = new Url();
      beakUrl.setUrl(url.url);
      beakUrl.setDisplayUrl(url.display_url);

      mappedUrls.add(beakUrl);
    }

    return mappedUrls;
  }

  private List<Media> mapMedia(List<TwitterMedia> mediaList) {
    if(mediaList == null) return null;

    ArrayList<Media> beakMedia = new ArrayList<>();

    for(TwitterMedia mediaElement: mediaList){
      Media media = new Media();
      media.setUrl(mediaElement.url);
      media.setDisplayUrl(mediaElement.display_url);

      beakMedia.add(media);
    }

    return beakMedia;
  }
}
