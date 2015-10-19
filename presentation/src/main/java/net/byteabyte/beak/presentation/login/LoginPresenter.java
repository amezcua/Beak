package net.byteabyte.beak.presentation.login;

import net.byteabyte.beak.domain.Twitter;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientInput;
import net.byteabyte.beak.domain.login.request_token.GetLoginRequestTokenAction;
import net.byteabyte.beak.domain.login.request_token.RequestTokenClientResponse;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenAction;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientException;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientInput;
import net.byteabyte.beak.domain.login.verify_token.VerifyTokenClientResponse;
import net.byteabyte.beak.domain.oauth.RedirectUrlParser;
import net.byteabyte.beak.presentation.OutputThread;
import net.byteabyte.beak.presentation.Presenter;

public class LoginPresenter extends Presenter<LoginView> {

  private final String consumerKey;
  private final String consumerSecret;

  private final GetLoginRequestTokenAction getLoginRequestTokenAction;
  private final VerifyTokenAction verifyTokenAction;

  public LoginPresenter(OutputThread outputThread,
      GetLoginRequestTokenAction getLoginRequestTokenAction,
      VerifyTokenAction verifyTokenAction,
      String consumerKey, String consumerSecret){
    super(outputThread);

    this.getLoginRequestTokenAction = getLoginRequestTokenAction;
    this.verifyTokenAction = verifyTokenAction;
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
  }

  public void onResume() {
    getView().showLoading();

    getView().initUi();

    retrieveOauthLoginToken();
  }

  private void retrieveOauthLoginToken() {
    runOnBackgroundThread(new Runnable() {
      @Override public void run() {
        getLoginRequestTokenAction.setRequestData(
            new RequestTokenClientInput(consumerKey, consumerSecret, Twitter.REDIRECT_URL));
        try {
          RequestTokenClientResponse loginResponse = getLoginRequestTokenAction.call();
          getOutputThread().execute(
              new OnGetLoginRequestTokenSuccess(loginResponse.buildLoginUrl()));
        } catch (Exception e) {
          getOutputThread().execute(new OnGetLoginRequestTokenError());
        }
      }
    });
  }

  public void onRedirectUrlReceived(final String url) {
    runOnBackgroundThread(new Runnable() {
      @Override public void run() {

        RedirectUrlParser.RedirectUserParseResult urlPars = new RedirectUrlParser().parse(url);

        verifyTokenAction.setRequestData(new VerifyTokenClientInput(consumerKey, consumerSecret, urlPars.getOauthRequestToken(), urlPars.getOauthVerifier()));

        try {
          VerifyTokenClientResponse verifyTokenClientResponse = verifyTokenAction.call();
          getOutputThread().execute(new OnVerifyLoginTokenSuccess(verifyTokenClientResponse.getOauthToken(), verifyTokenClientResponse.getOauthSecret()));
        } catch (VerifyTokenClientException e) {
          getOutputThread().execute(new OnVerifyTokenError());
        }
      }
    });
  }

  private class OnGetLoginRequestTokenSuccess implements Runnable{
    private final String token;

    public OnGetLoginRequestTokenSuccess(String token){
      this.token = token;
    }

    @Override public void run() {
      getView().hideLoading();
      getView().showLoginPage(this.token);
    }
  }

  private class OnGetLoginRequestTokenError implements Runnable{
    @Override public void run() {
      getView().onRetrieveLoginTokenError();
    }
  }

  private class OnVerifyLoginTokenSuccess implements Runnable{

    private final String oauthToken;
    private final String oauthSecret;

    public OnVerifyLoginTokenSuccess(String oauthToken, String oauthSecret){
      this.oauthToken = oauthToken;
      this.oauthSecret = oauthSecret;
    }

    @Override public void run() {
      getView().onLoginVerificationComplete(oauthToken, oauthSecret);
    }
  }

  private class OnVerifyTokenError implements Runnable{
    @Override public void run() {
      getView().onVerifyTokenError();
    }
  }
}
