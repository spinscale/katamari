package io.katamari.handler;

import com.ning.http.client.Request;
import com.ning.http.client.Response;
import io.katamari.settings.Settings;
import io.katamari.test.KatamariTest;
import org.junit.Test;
import sun.misc.BASE64Encoder;

/**
 *
 */
public class AuthDecoderTest extends KatamariTest {

  private final Settings settings = new Settings.SettingsBuilder().put("user", "user").put("pass", "pass").build();
  private final BASE64Encoder base64Encoder = new BASE64Encoder();

  @Override
  public void configure() {
    addHandler("auth:decoder", new AuthDecoder(".*", settings));
    addHandler("any:response", new HelloWorld());
  }

  @Test
  public void testThatAuthIsDeniedWithWrongUser() throws Exception {
    Request request = GET().addHeader("Authorization", encodeBasicAuth("noUser", "pass")).build();
    Response response = sendRequest(request);
    assertIsForbidden(response);
  }

  @Test
  public void testThatAuthIsDeniedWithWrongPass() throws Exception {
    Request request = GET().addHeader("Authorization", encodeBasicAuth("user", "wrongpass")).build();
    Response response = sendRequest(request);
    assertIsForbidden(response);
  }

  @Test
  public void testThatAuthIsDeniedWithArbitraryString() throws Exception {
    Request request = GET().addHeader("Authorization", "dafuq").build();
    Response response = sendRequest(request);
    assertIsForbidden(response);
  }

  @Test
  public void testThatAuthIsAllowedWithValidUserAndPass() throws Exception {
    Request request = GET().addHeader("Authorization", encodeBasicAuth("user", "pass")).build();
    Response response = sendRequest(request);
    assertIsOk(response);
  }

  private String encodeBasicAuth(String user, String pass) {
    return "Basic " + base64Encoder.encode(new String (user +  ":" + pass).getBytes());
  }

}
