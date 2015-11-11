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

    return new Tweet(networkTweet.created_at, networkTweet.id_str,
        mapMedia(networkTweet.entities.media), networkTweet.text,
        mapUser(networkTweet.user), mapUrls(networkTweet.entities.urls));
  }

  private User mapUser(TwitterUser twitterUser) {
    if(twitterUser == null) return null;

    return new User(twitterUser.name, twitterUser.screen_name, twitterUser.location, twitterUser.description,
        twitterUser.url, twitterUser.followers_count, twitterUser.friends_count,
        twitterUser.verified, twitterUser.statuses_count, twitterUser.profile_image_url, twitterUser.following);
  }

  private List<Url> mapUrls(List<TwitterUrl> urls) {
    if(urls == null) return null;

    ArrayList<Url> mappedUrls = new ArrayList<>();

    for(TwitterUrl url: urls){
      mappedUrls.add(new Url(url.url, url.display_url));
    }

    return mappedUrls;
  }

  private List<Media> mapMedia(List<TwitterMedia> mediaList) {
    if(mediaList == null) return null;

    ArrayList<Media> beakMedia = new ArrayList<>();

    for(TwitterMedia mediaElement: mediaList){
      beakMedia.add(new Media(mediaElement.url, mediaElement.display_url));
    }

    return beakMedia;
  }
}
