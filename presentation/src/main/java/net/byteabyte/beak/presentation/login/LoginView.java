package net.byteabyte.beak.presentation.login;

public interface LoginView {
  void showLoading();

  void hideLoading();

  void showLoginPage(String url);

  void onRetrieveLoginTokenError();

  void initUi();

  void onVerifyTokenError();

  void onLoginVerificationComplete(String oauthToken, String oauthSecret);
}
