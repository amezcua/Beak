package net.byteabyte.beak.home_timeline;

import net.byteabyte.beak.domain.models.User;

public interface TweetTimelineListener {
  void onDisplayUserDetails(TweetViewHolder tweetViewHolder, User user);
}
