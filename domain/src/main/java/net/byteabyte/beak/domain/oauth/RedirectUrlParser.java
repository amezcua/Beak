package net.byteabyte.beak.domain.oauth;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import net.byteabyte.beak.domain.StringUtils;

public class RedirectUrlParser{

  public RedirectUserParseResult parse(String redirectUrl){
    if(StringUtils.isNullOrEmpty(redirectUrl)) throw new IllegalArgumentException("The redirection url can not be empty.");

    try {
      return parseRedirectUrl(redirectUrl);
    }catch (Exception e){
      throw new IllegalArgumentException("Can not parse the url. Please verify its format.", e);
    }
  }

  private RedirectUserParseResult parseRedirectUrl(String redirectUrl) {
    URI uri = URI.create(redirectUrl);

    String queryItems[] = uri.getQuery().split("&");

    Map<String, String> pars = new HashMap<>();

    for(String item: queryItems){
      String kv[] = item.split("=");
      pars.put(kv[0], kv[1]);
    }

    return new RedirectUserParseResult(pars.get(OauthKeys.OAUTH_TOKEN.getKey()), pars.get(OauthKeys.OAUTH_VERIFIER.getKey()));
  }

  public class RedirectUserParseResult{
    private final String oauthRequestToken;
    private final String oauthVerifier;

    public RedirectUserParseResult(String oauthRequestToken, String oauthVerifier) {
      this.oauthRequestToken = oauthRequestToken;
      this.oauthVerifier = oauthVerifier;
    }

    public String getOauthRequestToken() {
      return oauthRequestToken;
    }

    public String getOauthVerifier() {
      return oauthVerifier;
    }
  }
}