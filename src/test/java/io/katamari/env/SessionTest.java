package io.katamari.env;

import io.katamari.utils.Crypto;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SessionTest {

  private final String secret = "myUltraSecret";
  private final Session session = new Session();

  @Test
  public void testThatAddingFieldsWorks() throws Exception {
    session.put("foo", "bar");
    assertThat(session.serialize(secret), containsString("foo=bar"));
  }

  @Test
  public void testThatClearingWorks() throws Exception {
    session.put("foo", "bar");
    assertThat(session.isEmpty(), is(false));
    session.clear();
    assertThat(session.isEmpty(), is(true));
    assertThat(session.serialize(secret), isEmptyString());
  }

  @Test
  public void testThatRemovingWorks() throws Exception {
    session.put("foo", "bar");
    session.remove("foo");
    assertThat(session.serialize(secret), isEmptyString());
  }

  @Test
  public void testThatDeserializationWorks() throws Exception {
    String value = "foo=bar&spam=eggs";
    session.deserialize(secret, Crypto.sign(value, secret.getBytes()) + value);
    assertThat(session.get("foo"), is("bar"));
    assertThat(session.get("spam"), is("eggs"));
  }

  @Test
  public void testThatSessionAlterationThrowsException() throws Exception {
    String originalSession = "foo=bar&spam=eggs";
    String originalHash = Crypto.sign(originalSession, secret.getBytes());
    String changedSession = "foo=bar&spam=egg";
    session.deserialize(secret,  originalHash + changedSession);
    assertThat(session.serialize(secret), isEmptyString());
  }

}
