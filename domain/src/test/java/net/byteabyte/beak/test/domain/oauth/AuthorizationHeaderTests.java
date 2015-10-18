package net.byteabyte.beak.test.domain.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.byteabyte.beak.domain.oauth.AuthorizationHeader;
import net.byteabyte.beak.domain.oauth.AuthorizationHeaderCreationException;
import net.byteabyte.beak.domain.oauth.NonceGenerator;
import net.byteabyte.beak.domain.oauth.OauthKeys;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthorizationHeaderTests {

  private final static String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
  private static final String OAUTH_TOKEN = "oauth_token";
  private static final String OAUTH_VERSION = "oauth_version";
  private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
  private static final String OAUTH_SIGNATURE = "oauth_signature";
  private static final String OAUTH_NONCE = "oauth_nonce";
  private static final String OAUTH_TIMESTAMP = "oauth_timestamp";

  String consumerKey = "xvz1evFS4wEEPTGEFPHBog";
  String consumerSecret = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
  String oauthToken = "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb";
  String oauthTokenSecret = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
  String requestMethod = "POST";
  String url = "https://api.twitter.com/1/statuses/update.json?/1/statuses/update.json?include_entities=true";
  String requestBody = "status=Hello Ladies + Gentlemen, a signed OAuth request!";
  String header;

  @Before public void generateHeader() throws AuthorizationHeaderCreationException {
    header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withBody(requestBody)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .build();
  }

  @Test(expected = AuthorizationHeaderCreationException.class)
  public void canNotCreateHeaderWithoutKeyTokenMethodUrlAndBody()
      throws AuthorizationHeaderCreationException {
    header = new AuthorizationHeader.Builder().build();
  }

  @Test public void canCreateHeaderWithConsumerKeyAndToken(){
    assertNotNull(header);
  }

  @Test public void generatedHeaderStartsWithOauth(){
    assertTrue(header.startsWith("OAuth "));
  }

  @Test public void generatedHeaderContainsConsumerKey(){
    String expectedHeaderValue = OAUTH_CONSUMER_KEY + "=\"" + consumerKey + "\"";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsNonce(){
    String expectedHeaderValue = OAUTH_NONCE + "=";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsSignature(){
    String expectedHeaderValue = OAUTH_SIGNATURE + "=";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsSignatureMethod(){
    String expectedHeaderValue = OAUTH_SIGNATURE_METHOD + "=\"HMAC-SHA1\"";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsTimestap(){
    String expectedHeaderValue = OAUTH_TIMESTAMP + "=";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsOauthToken(){
    String expectedHeaderValue = OAUTH_TOKEN + "=\"" + oauthToken + "\"";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderContainsOauthVersion(){
    String expectedHeaderValue = OAUTH_VERSION + "=\"1.0\"";

    assertTrue(header.contains(expectedHeaderValue));
  }

  @Test public void generatedHeaderHasTheRightAmountOfFields(){
    int fieldCount = 9;

    assertEquals(fieldCount, header.split(",").length);
  }

  @Test public void nonceHasAValue(){
    HashMap<String, String> headerValues = convertHeaderFields(header);

    assertFalse(isNullOrEmpty(headerValues.get(OAUTH_NONCE)));
  }

  @Test public void timeStampHasAValue(){
    HashMap<String, String> headerValues = convertHeaderFields(header);

    assertFalse(isNullOrEmpty(headerValues.get(OAUTH_TIMESTAMP)));
  }

  @Test public void signatureHasAValue(){
    HashMap<String, String> headerValues = convertHeaderFields(header);

    assertFalse(isNullOrEmpty(headerValues.get(OAUTH_SIGNATURE)));
  }

  @Test public void canCreateHeaderForUrisWithoutQueryParams()
      throws AuthorizationHeaderCreationException {
    String url = "https://api.twitter.com/path";
    String header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withBody(requestBody)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .build();

    assertNotNull(header);
  }

  @Test public void signatureHasValidValue()
      throws AuthorizationHeaderCreationException, NoSuchAlgorithmException {
    String testNonce = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg";
    String testTimeStamp = "1318622958";
    String testSignature = "tnnArxj06cWHq44gCs1OSKk%2FjLY%3D";

    NonceGenerator nonceGenerator = mock(NonceGenerator.class);
    when(nonceGenerator.createNonce()).thenReturn(testNonce);

    String header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withBody(requestBody)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .usingNonceGenerator(nonceGenerator)
        .build(testTimeStamp);

    HashMap<String, String> headerValues = convertHeaderFields(header);

    assertEquals(testSignature, headerValues.get(OAUTH_SIGNATURE));
  }

  @Test public void headerDoesNotEndInSeparator(){
    assertFalse(header.endsWith(","));
  }

  @Test public void mockNonceValuePresentInHeader()
      throws NoSuchAlgorithmException, AuthorizationHeaderCreationException {
    String nonce = UUID.randomUUID().toString();

    NonceGenerator nonceGenerator = mock(NonceGenerator.class);
    when(nonceGenerator.createNonce()).thenReturn(nonce);

    String testHeader = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withBody(requestBody)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .usingNonceGenerator(nonceGenerator)
        .build();

    HashMap<String, String> headerFields = convertHeaderFields(testHeader);

    assertEquals(nonce, headerFields.get(OAUTH_NONCE));
  }

  @Test public void passedTimeStampIsPresentInHeader()
      throws NoSuchAlgorithmException, AuthorizationHeaderCreationException, InterruptedException {
    String timeStamp = String.valueOf(System.currentTimeMillis());

    Thread.sleep(10);
    String testHeader = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withBody(requestBody)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .build(timeStamp);

    HashMap<String, String> headerFields = convertHeaderFields(testHeader);

    assertEquals(timeStamp, headerFields.get(OAUTH_TIMESTAMP));
  }

  @Test public void keysAreSortedAlphabetically(){
    String [] fields = header.split(",");

    for(int i=0; i<fields.length-1; i++){
      assertTrue(fields[i].compareTo(fields[i+1]) <= 0);
    }
  }

  @Test public void canCreateHeaderWithoutBody() throws AuthorizationHeaderCreationException {
    String header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .build();

    assertNotNull(header);
  }

  @Test public void canSignRequestsWithoutOauthToken() throws AuthorizationHeaderCreationException {
    String header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withConsumerKeys(consumerKey, consumerSecret)
        .build();

    assertNotNull(header);

    HashMap<String, String> headerFields = convertHeaderFields(header);
    assertFalse(headerFields.containsKey(OAUTH_TOKEN));
  }

  @Test public void canAddExtraParametersToTheHeaderNotInUrlNorBody()
      throws AuthorizationHeaderCreationException {

    String extraKey = OauthKeys.OAUTH_CALLBACK.getKey();
    String extraKeyValue = "beak://test_callback";

    Map<String, String> extraParameters = new HashMap<>();
    extraParameters.put(extraKey, extraKeyValue);

    String header = new AuthorizationHeader.Builder()
        .forRequestMethod(requestMethod)
        .forUrl(url)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withOauthToken(oauthToken, oauthTokenSecret)
        .withExtraParameters(extraParameters)
        .build();

    HashMap<String, String> headerFields = convertHeaderFields(header);

    assertEquals(percentEncode(extraKeyValue), headerFields.get(extraKey));
  }

  @Test public void testSignatureWithoutOauthTokens()
      throws AuthorizationHeaderCreationException, NoSuchAlgorithmException {
    String url = "https://api.twitter.com/oauth/request_token";
    String nonce = "ff49f7c4fad5ac6e5f1013b4cb93c2b6";
    String callback = "oob";
    String timestamp = "1444163505";
    String consumerKey = "KxwHvrwLd9U1nxmcIJfW1dwhx";
    String consumerSecret = "0YYthMakVuwlU6HPr7iJd1N9TGKHcNPmBvlm7HZJhZMoU4z1Wi";
    String expectedSignature = "D%2FxHp3RBtHobXKLWKiJVJJfgp4E%3D";

    Map<String, String> extraParameters = new HashMap<>();
    extraParameters.put("oauth_callback", callback);

    NonceGenerator nonceGenerator = mock(NonceGenerator.class);
    when(nonceGenerator.createNonce()).thenReturn(nonce);

    String testHeader = new AuthorizationHeader.Builder()
        .forRequestMethod("POST")
        .forUrl(url)
        .withConsumerKeys(consumerKey, consumerSecret)
        .withExtraParameters(extraParameters)
        .usingNonceGenerator(nonceGenerator)
        .build(timestamp);

    HashMap<String, String> headerFields = convertHeaderFields(testHeader);

    assertEquals(expectedSignature, headerFields.get(OAUTH_SIGNATURE));
  }

  private HashMap<String, String> convertHeaderFields(String header){
    HashMap<String, String> output = new HashMap<>();

    header = header.replace("OAuth ", "");

    String[] headerFields = header.split(",");
    for(String field: headerFields){
      String[] kv = field.split("=");
      output.put(kv[0], kv[1].replace("\"", ""));
    }

    return output;
  }

  public static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().length() ==0;
  }

  private String percentEncode(String s) {
    if (s == null) {
      return "";
    }
    try {
      return URLEncoder.encode(s, "UTF-8")
          // OAuth encodes some characters differently:
          .replace("+", "%20").replace("*", "%2A")
          .replace("%7E", "~");
      // This could be done faster with more hand-crafted code.
    } catch (UnsupportedEncodingException wow) {
      throw new RuntimeException(wow.getMessage(), wow);
    }
  }
}