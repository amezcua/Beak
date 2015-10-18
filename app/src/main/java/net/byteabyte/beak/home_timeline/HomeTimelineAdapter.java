package net.byteabyte.beak.home_timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;
import net.byteabyte.beak.domain.models.Tweet;

public class HomeTimelineAdapter extends RecyclerView.Adapter<TweetViewHolder> {

  private final Context context;
  private final TweetTimelineListener tweetTimelineListener;
  private List<Tweet> timeline;

  public HomeTimelineAdapter(Context context, TweetTimelineListener tweetTimelineListener){
    this.context = context;
    this.tweetTimelineListener = tweetTimelineListener;
  }

  public void setTimeline(List<Tweet> timeline){
    this.timeline = timeline;
  }

  @Override public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TweetViewHolder(context, parent, tweetTimelineListener);
  }

  @Override public void onBindViewHolder(TweetViewHolder holder, int position) {
    holder.displayTweet(timeline.get(position));
  }

  @Override public int getItemCount() {
    return this.timeline == null ? 0 : this.timeline.size();
  }
}