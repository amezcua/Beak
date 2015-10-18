package net.byteabyte.beak.home_timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import net.byteabyte.beak.R;
import net.byteabyte.beak.domain.models.Tweet;
import net.byteabyte.beak.domain.models.User;
import net.byteabyte.beak.presentation.TimeAgo;

public class TweetViewHolder extends RecyclerView.ViewHolder {

  private final Context context;
  private final TweetTimelineListener tweetTimelineListener;

  @Bind(R.id.user_image) ImageView userImage;
  @Bind(R.id.user_name) TextView userName;
  @Bind(R.id.user_display_name) TextView userDisplayName;
  @Bind(R.id.tweetDate) TextView tweetDate;
  @Bind(R.id.tweetText) TextView tweetText;

  public TweetViewHolder(Context context, ViewGroup parent,
      final TweetTimelineListener tweetTimelineListener) {
    super(LayoutInflater.from(context).inflate(R.layout.tweet_list_item, parent, false));

    this.context = context;
    this.tweetTimelineListener = tweetTimelineListener;
    ButterKnife.bind(this, itemView);
  }

  public void displayTweet(final Tweet tweet){

    final User user = tweet.getUser();

    Glide.with(context)
        .load(user.getProfileImage())
        .centerCrop()
        .placeholder(R.drawable.user_placeholder)
        .crossFade()
        .into(userImage);

    userName.setText(user.getName());
    userDisplayName.setText("@" + user.getScreenName());
    tweetDate.setText(new TimeAgo().timeAgo(tweet.getCreated()));
    tweetText.setText(Html.fromHtml(tweet.getText()));
    tweetText.setMovementMethod(LinkMovementMethod.getInstance());

    userImage.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (tweetTimelineListener == null) return;
        tweetTimelineListener.onDisplayUserDetails(TweetViewHolder.this, tweet.getUser());
      }
    });

    userName.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (tweetTimelineListener == null) return;
        tweetTimelineListener.onDisplayUserDetails(TweetViewHolder.this, tweet.getUser());
      }
    });
  }
}
