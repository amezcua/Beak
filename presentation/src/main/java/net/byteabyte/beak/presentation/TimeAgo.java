package net.byteabyte.beak.presentation;

import java.util.Date;

public class TimeAgo {

  public String timeAgo(Date date) {
    return timeAgo(date.getTime());
  }

  public String timeAgo(long millis) {
    long diff = new Date().getTime() - millis;

    double seconds = Math.abs(diff) / 1000;
    double minutes = seconds / 60;
    double hours = minutes / 60;
    double days = hours / 24;
    double years = days / 365;

    String words;

    if (seconds < 45) {
      words = "1s";
    } else if (seconds < 90) {
      words = "1m";
    } else if (minutes < 45) {
      words = String.format("%dm", Math.round(minutes));
    } else if (minutes < 90) {
      words = "1h";
    } else if (hours < 24) {
      words = String.format("%dh", Math.round(hours));
    } else if (hours < 42) {
      words = "1d";
    } else if (days < 30) {
      words = String.format("%dd", Math.round(days));
    } else if (days < 45) {
      words = "1M";
    } else if (days < 365) {
      words = String.format("%dM", Math.round(days / 30));
    } else if (years < 1.5) {
      words = "1y";
    } else {
      words = String.format("%dy", Math.round(years));
    }

    StringBuilder sb = new StringBuilder();

    sb.append(words);

    return sb.toString().trim();
  }
}
