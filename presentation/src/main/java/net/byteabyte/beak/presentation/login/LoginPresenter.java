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
import net.byteabyte.beak.presentation.common.BackgroundTask;
import net.byteabyte.beak.presentation.common.OutputThread;
import net.byteabyte.beak.presentation.common.Presenter;

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

  @BackgroundTask
  private void retrieveOauthLoginToken() {
    getLoginRequestTokenAction.setRequestData(
        new RequestTokenClientInput(consumerKey, consumerSecret, Twitter.REDIRECT_URL));
    try {
      RequestTokenClientResponse loginResponse = getLoginRequestTokenAction.call();
      getView().hideLoading();
      getView().showLoginPage(loginResponse.buildLoginUrl());
    } catch (Exception e) {
      getView().onRetrieveLoginTokenError();
    }
  }

  @BackgroundTask
  public void onRedirectUrlReceived(final String url) {
    RedirectUrlParser.RedirectUserParseResult urlPars = new RedirectUrlParser().parse(url);

    verifyTokenAction.setRequestData(new VerifyTokenClientInput(consumerKey, consumerSecret, urlPars.getOauthRequestToken(), urlPars.getOauthVerifier()));

    try {
      VerifyTokenClientResponse verifyTokenClientResponse = verifyTokenAction.call();
      getView().onLoginVerificationComplete(verifyTokenClientResponse.getOauthToken(),
          verifyTokenClientResponse.getOauthSecret());
    } catch (VerifyTokenClientException e) {
      getView().onVerifyTokenError();
    }
  }
}
