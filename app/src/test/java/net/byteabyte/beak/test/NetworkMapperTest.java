package net.byteabyte.beak.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.network.models.twitter.NetworkMapper;
import net.byteabyte.beak.network.models.twitter.TwitterEntities;
import net.byteabyte.beak.network.models.twitter.TwitterMedia;
import net.byteabyte.beak.network.models.twitter.TwitterTweet;
import net.byteabyte.beak.network.models.twitter.TwitterUrl;
import net.byteabyte.beak.network.models.twitter.TwitterUser;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkMapperTest {

  @Test public void mapOfNullNetworkObjectReturnsNull(){
    NetworkMapper mapper = new NetworkMapper();

    TwitterTweet networkTweet = null;

    assertNull(mapper.mapTweet(networkTweet));
  }

  @Test public void mapOfEmptyNetworkObjectReturnsEmptyDomainObject(){
    NetworkMapper mapper = new NetworkMapper();

    TwitterTweet networkTweet = new TwitterTweet();
    TwitterEntities entities = new TwitterEntities();
    networkTweet.entities = entities;

    assertNotNull(mapper.mapTweet(networkTweet));
  }

  @Test public void mapOfNetworkObjectReturnsDomainObject(){
    String userName = "testName";
    String userDescription = "testDescription";
    String userLocation = "testLocation";
    String userProfileImage = "profile image";
    String userScreenName = "ScreenName";
    String userUrl = "http://testUrl";
    int userFollowersCount = 2;
    int userFriendsCount = 20;
    int userStatusesCount = 50;

    TwitterUser twitterUser = createTestTwitterUser(userName, userDescription, userLocation, userProfileImage,
        userScreenName, userUrl, userFollowersCount, userFriendsCount, userStatusesCount);

    TwitterUrl testUrl = new TwitterUrl();
    testUrl.url = "http://test";
    testUrl.display_url = "http://test_display";

    ArrayList<TwitterUrl> testUrls = new ArrayList<>();
    testUrls.add(testUrl);

    TwitterMedia testMedia = new TwitterMedia();
    testMedia.url = "http://testMedia";
    testMedia.display_url = "http://testMedia_display";

    ArrayList<TwitterMedia> testMediaElements = new ArrayList<>();
    testMediaElements.add(testMedia);


    String id = "tweetid";
    Date tweetDate = new Date();
    String tweetText = "tweetText";

    TwitterTweet networkTweet = createTestTweet(id, tweetDate, tweetText, twitterUser, testUrls, testMediaElements);

    NetworkMapper mapper = new NetworkMapper();
    Tweet tweet = mapper.mapTweet(networkTweet);

    assertEquals(id, tweet.getId());
    assertEquals(tweetDate, tweet.getCreated());
    assertEquals(tweetText, tweet.getText());
    assertEquals(userName, tweet.getUser().getName());
    assertEquals(userDescription, tweet.getUser().getDescription());
    assertEquals(userLocation, tweet.getUser().getLocation());
    assertEquals(userProfileImage, tweet.getUser().getProfileImage());
    assertEquals(userScreenName, tweet.getUser().getScreenName());
    assertEquals(userUrl, tweet.getUser().getUrl());
    assertEquals(userFollowersCount, tweet.getUser().getFollowersCount());
    assertEquals(userFriendsCount, tweet.getUser().getFriendsCount());
    assertEquals(userStatusesCount, tweet.getUser().getStatusesCount());

    assertTrue(tweet.getUrls().size() == 1);
    assertTrue(tweet.getMedia().size() == 1);
  }

  private TwitterUser createTestTwitterUser(String name, String description,
      String userLocation, String userProfileImage,
      String userScreenName, String userUrl,
      int userFollowersCount, int userFriendsCount, int userStatusesCount){

    TwitterUser user = new TwitterUser();

    user.name = name;
    user.description = description;
    user.location = userLocation;
    user.profile_image_url = userProfileImage;
    user.screen_name = userScreenName;
    user.url = userUrl;
    user.followers_count = userFollowersCount;
    user.friends_count = userFriendsCount;
    user.statuses_count = userStatusesCount;

    return user;
  }

  private TwitterTweet createTestTweet(String id, Date tweetDate, String tweetText, TwitterUser user, List<TwitterUrl> urls,
      List<TwitterMedia> media){

    TwitterTweet networkTweet = new TwitterTweet();
    networkTweet.created_at = tweetDate;
    networkTweet.id_str = id;
    networkTweet.text = tweetText;
    networkTweet.user = user;

    networkTweet.entities = new TwitterEntities();
    networkTweet.entities.urls = urls;
    networkTweet.entities.media = media;

    return networkTweet;
  }
}