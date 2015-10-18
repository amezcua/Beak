package net.byteabyte.beak.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import net.byteabyte.beak.BeakActivityBase;
import net.byteabyte.beak.BuildConfig;
import net.byteabyte.beak.R;
import net.byteabyte.beak.UIThreadOutput;
import net.byteabyte.beak.domain.Twitter;
import net.byteabyte.beak.domain.login.request_token.GetLoginRequestTokenAction;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenAction;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import net.byteabyte.beak.login.request_token.GetLoginTokenClient;
import net.byteabyte.beak.login.verify_token.VerifyTokenClient;
import net.byteabyte.beak.presentation.login.LoginPresenter;
import net.byteabyte.beak.presentation.login.LoginView;

public class LoginActivity extends BeakActivityBase implements LoginView {

  public static final int LOGIN_REQUEST_CODE = 1234;

  private LoginPresenter presenter;

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.loading) View loadingProgress;
  @Bind(R.id.webView) WebView webView;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.login_dialog);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    presenter = new LoginPresenter(new UIThreadOutput(),
        new GetLoginRequestTokenAction(new GetLoginTokenClient()),
        new VerifyTokenAction(new VerifyTokenClient()),
        BuildConfig.twitterConsumerKey,
        BuildConfig.twitterConsumerSecret);
    presenter.attachView(this);
  }

  @Override public void onResume() {
    super.onResume();

    presenter.onResume();
  }

  @Override public void onDestroy() {
    presenter.detachView();
    super.onDestroy();
  }

  @Override public void initUi(){
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.toLowerCase().startsWith(Twitter.REDIRECT_URL)){
          presenter.onRedirectUrlReceived(url);
          return true;
        }else {
          return false;
        }
      }
    });
  }

  @Override public void showLoading() {
    loadingProgress.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    loadingProgress.setVisibility(View.GONE);
  }

  @Override public void showLoginPage(String url) {
    webView.loadUrl(url);
    webView.setVisibility(View.VISIBLE);
  }

  @Override public void onRetrieveLoginTokenError() {
    Toast.makeText(this, "Error retrieving token", Toast.LENGTH_SHORT).show();
  }

  @Override public void onVerifyTokenError() {
    Toast.makeText(this, "Error verifying the login token Please try again.", Toast.LENGTH_SHORT).show();
  }

  @Override public void onLoginVerificationComplete(String oauthToken, String oauthSecret) {
    Intent resultIntent = new Intent();
    resultIntent.putExtra(OauthKeys.OAUTH_TOKEN.getKey(), oauthToken);
    resultIntent.putExtra(OauthKeys.OAUTH_TOKEN_SECRET.getKey(), oauthSecret);

    setResult(RESULT_OK, resultIntent);
    finish();
  }
}