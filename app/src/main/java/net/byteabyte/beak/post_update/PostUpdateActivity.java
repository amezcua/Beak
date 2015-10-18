package net.byteabyte.beak.post_update;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import net.byteabyte.beak.BeakActivityBase;
import net.byteabyte.beak.BuildConfig;
import net.byteabyte.beak.R;
import net.byteabyte.beak.UIThreadOutput;
import net.byteabyte.beak.domain.post_update.PostUpdateAction;
import net.byteabyte.beak.presentation.post_update.PostUpdatePresenter;
import net.byteabyte.beak.presentation.post_update.PostUpdateView;

public class PostUpdateActivity extends BeakActivityBase implements PostUpdateView {

  public static final int REQUEST_CODE = 4325;
  public static final String EXTRA_OAUTH_TOKEN = "oauth_token";
  public static final String EXTRA_OAUTH_SECRET = "oauth_secret";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.new_status_text) EditText statusText;
  @Bind(R.id.post_status_button) Button postStatusButton;

  private PostUpdatePresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String oauthToken = getIntent().getStringExtra(EXTRA_OAUTH_TOKEN);
    String oauthSecret = getIntent().getStringExtra(EXTRA_OAUTH_SECRET);

    presenter = new PostUpdatePresenter(new UIThreadOutput(),
        new PostUpdateAction(new PostStatusUpdateClient()),
        BuildConfig.twitterConsumerKey,
        BuildConfig.twitterConsumerSecret,
        oauthToken,
        oauthSecret);

    setContentView(R.layout.activity_post_update);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    postStatusButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.postStatusUpdate(statusText.getText().toString().trim());
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();

    presenter.attachView(this);
  }

  @Override protected void onDestroy() {
    presenter.detachView();
    super.onDestroy();
  }

  @Override public void onStatusEmptyError() {
    Toast.makeText(this, R.string.status_text_error, Toast.LENGTH_SHORT).show();
  }

  @Override public void onStatusPosted() {
    Toast.makeText(this, R.string.status_text_posted, Toast.LENGTH_SHORT).show();
    setResult(RESULT_OK);
    finish();
  }
}
