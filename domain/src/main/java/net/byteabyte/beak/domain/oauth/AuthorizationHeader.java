package net.byteabyte.beak.domain.oauth;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import net.byteabyte.beak.domain.StringUtils;

public class AuthorizationHeader {

  private static final String HEADER_PREFIX = "OAuth ";
  private static final String OAUTH_VERSION_VALUE = "1.0";
  private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
  private static final String FIELD_SEPARATOR = ",";

  public static class Builder {

    private NonceGenerator nonceGenerator;
    private String timeStamp;

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String requestMethod;
    private String requestUrl;
    private String requestBody;
    private Map<String, String> extraParameters;

    TreeMap<String, String> header;

    public Builder withConsumerKeys(String consumerKey, String consumerSecret) {
      this.consumerKey = consumerKey;
      this.consumerSecret = consumerSecret;

      return this;
    }

    public Builder withOauthToken(String token, String tokenSecret) {
      this.accessToken = token;
      this.accessTokenSecret = tokenSecret;

      return this;
    }

    public Builder forRequestMethod(String requestMethod){
      this.requestMethod = requestMethod;

      return this;
    }

    public Builder forUrl(String url){
      this.requestUrl = url;

      return this;
    }

    public Builder withBody(String body){
      this.requestBody = body;

      return this;
    }

    public Builder withExtraParameters(Map<String, String> extraParameters) {
      this.extraParameters = extraParameters;

      return this;
    }

    public Builder usingNonceGenerator(NonceGenerator generator){
      this.nonceGenerator = generator;

      return this;
    }

    public String build(String timeStamp) throws AuthorizationHeaderCreationException{
      this.timeStamp = timeStamp;
      return build();
    }

    public String build() throws AuthorizationHeaderCreationException {
      if(!hasRequiredParameters()) throw new AuthorizationHeaderCreationException();

      header = new TreeMap<>();

      appendConsumerKey(header);
      appendNonce(header);
      appendOauthSignatureVersion(header);
      appendOauthTimestamp(header);
      appendOauthToken(header);
      appendOauthVersion(header);
      appendUrlParameters(header);
      appendBodyParameters(header);
      appendExtraParameters(header);
      appendOauthSignature(header);

      return buildHeaderString(header);
    }

    private void appendBodyParameters(TreeMap<String, String> header) {
      if(requestBody != null) {
        header.putAll(extractBodyParameters(requestBody));
      }

    }

    private void appendUrlParameters(TreeMap<String, String> header) {
      header.putAll(extractUrlParameters(requestUrl));
    }

    private String buildHeaderString(TreeMap<String, String> headerValues) {
      String outputHeader = HEADER_PREFIX;

      for(Map.Entry<String, String> kv: headerValues.entrySet()){
        outputHeader += PercentEncoder.encode(kv.getKey());
        outputHeader += "=\"";
        outputHeader += PercentEncoder.encode(kv.getValue());
        outputHeader += "\"";
        outputHeader += FIELD_SEPARATOR;
      }

      return outputHeader.substring(0, outputHeader.length() - 1);
    }

    private boolean hasRequiredParameters(){
      return
          !StringUtils.isNullOrEmpty(consumerKey) &&
          !StringUtils.isNullOrEmpty(consumerSecret) &&
          !StringUtils.isNullOrEmpty(requestMethod) &&
          !StringUtils.isNullOrEmpty(requestUrl);
    }

    private void appendConsumerKey(TreeMap<String, String> header) {
      header.put(OauthKeys.OAUTH_CONSUMER_KEY.getKey(), consumerKey);
    }

    private void appendNonce(TreeMap<String, String> header) throws AuthorizationHeaderCreationException {
      header.put(OauthKeys.OAUTH_NONCE.getKey(), generateNonce());
    }

    private void appendOauthToken(TreeMap<String, String> header){
      if(!StringUtils.isNullOrEmpty(accessToken)) header.put(OauthKeys.OAUTH_TOKEN.getKey(), accessToken);
    }

    private void appendOauthVersion(TreeMap<String, String> header){
      header.put(OauthKeys.OAUTH_VERSION.getKey(), OAUTH_VERSION_VALUE);
    }

    private void appendOauthSignature(TreeMap<String, String> header)
        throws AuthorizationHeaderCreationException {
      header.put(OauthKeys.OAUTH_SIGNATURE.getKey(), generateSignature(header));
    }

    private void appendExtraParameters(TreeMap<String, String> header) {
      if(extraParameters != null) header.putAll(extraParameters);
    }

    private void appendOauthSignatureVersion(TreeMap<String, String> header){
      header.put(OauthKeys.OAUTH_SIGNATURE_METHOD.getKey(), OAUTH_SIGNATURE_METHOD_VALUE);
    }

    private void appendOauthTimestamp(TreeMap<String, String> header){
      header.put(OauthKeys.OAUTH_TIMESTAMP.getKey(), generateTimeStamp());
    }

    private String generateNonce() throws AuthorizationHeaderCreationException {
      try {
        NonceGenerator generator = nonceGenerator == null ? new RandomNonceGenerator() : nonceGenerator;
        return generator.createNonce();
      } catch (NoSuchAlgorithmException e) {
        throw new AuthorizationHeaderCreationException(e);
      }
    }

    private String generateTimeStamp(){
      return StringUtils.isNullOrEmpty(timeStamp) ? String.valueOf(new Date().getTime() / 1000) : timeStamp;
    }

    private String generateSignature(TreeMap<String, String> headerValues)
        throws AuthorizationHeaderCreationException {

      try {
        String parameterString = createParameterString(headerValues);
        String valueToSign = createSignatureBaseString(parameterString);

        return new SignatureGenerator(consumerSecret, accessTokenSecret).computeHmacSha1(valueToSign);
      } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | URISyntaxException e) {
        throw new AuthorizationHeaderCreationException(e);
      }
    }

    private String createParameterString(TreeMap<String, String> headerValues){
      String parameterString = "";

      for(Map.Entry<String, String> kv: headerValues.entrySet()){
        String key = PercentEncoder.encode(kv.getKey());
        String value = PercentEncoder.encode(kv.getValue());

        parameterString += key;
        parameterString += "=";
        parameterString += value;
        parameterString += "&";
      }
      return parameterString.substring(0, parameterString.length() - 1);
    }

    private Map<String, String> extractUrlParameters(String requestUrl) {
      URI uri = URI.create(requestUrl);

      if(uri.getQuery() == null) return new HashMap<>(); // No parameters in the URL

      String uriPath = uri.getPath();
      String uriQuery = uri.getQuery().replace(uriPath, "");
      if(uriQuery.startsWith("?")) uriQuery = uriQuery.substring(1);

      return convertUrlEncodedStringToMap(uriQuery);
    }

    private Map<String, String> extractBodyParameters(String requestBody) {
      return convertUrlEncodedStringToMap(requestBody);
    }

    private Map<String, String> convertUrlEncodedStringToMap(String urlEncodedValue){
      HashMap<String, String> outputMap = new HashMap<>();

      for(String queryParam: urlEncodedValue.split("&")){
        String[] kv = queryParam.split("=");
        String key = kv[0];
        String value = kv[1];
        outputMap.put(key, value);
      }

      return outputMap;
    }

    private String createSignatureBaseString(String parameterString) throws URISyntaxException {
      return
          requestMethod.toUpperCase() + "&" +
          PercentEncoder.encode(extractBaseUrl(requestUrl)) + "&" +
          PercentEncoder.encode(parameterString);
    }

    private String extractBaseUrl(String url) throws URISyntaxException {
      URI uri = new URI(url);

      return uri.getScheme() + "://" + uri.getHost() + uri.getPath();
    }
  }
}
