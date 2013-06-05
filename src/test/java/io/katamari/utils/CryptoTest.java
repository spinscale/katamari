package io.katamari.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CryptoTest {

  @Test
  public void cryptoTest() throws Exception {
    String signedFoo = Crypto.sign("foo", "mySecret".getBytes());
    assertThat(signedFoo, is("72a57b0d742961e3fe95fff87fd578ed44e3e0fd"));
    assertThat(40, is("72a57b0d742961e3fe95fff87fd578ed44e3e0fd".length()));
  }
}
