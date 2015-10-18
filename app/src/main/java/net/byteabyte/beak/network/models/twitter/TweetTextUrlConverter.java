package net.byteabyte.beak.network.models.twitter;

import java.util.List;

public class TweetTextUrlConverter {

  public String convertUrls(TwitterTweet tt) {
    String originalText = tt.text;
    String convertedText;
    convertedText = updateTextWithUrls(originalText, tt.entities.urls);
    convertedText = updateRemovingMediaUrls(convertedText, tt.entities.media);

    return convertedText;
  }

  private String updateRemovingMediaUrls(String originalText, List<TwitterMedia> media) {
    String updatedText = originalText;

    if(media == null){
      return originalText;
    }else {

      for(TwitterMedia mediaUrl: media){
        updatedText = updatedText.replace(mediaUrl.url, "");
      }

      return updatedText;
    }
  }

  private String updateTextWithUrls(String originalText, List<TwitterUrl> urls) {
    String updatedText = originalText;

    if(urls == null){
      return originalText;
    }else{
      for(TwitterUrl url: urls){
        updatedText = updatedText.replace(url.url, buildHtmlLinkFor(url));
      }

      return updatedText;
    }
  }

  private String buildHtmlLinkFor(TwitterUrl url) {
    return "<a href=\"" + url.url.trim() + "\">" + url.display_url.trim() + "</a>";
  }
}
