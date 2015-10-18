package net.byteabyte.beak.domain.post_update;

import net.byteabyte.beak.domain.models.Tweet;

public interface PostUpdateClient {
  Tweet postUpdate(PostUpdateInput input) throws PostUpdateException;
}
