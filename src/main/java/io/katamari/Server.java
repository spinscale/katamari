package io.katamari;

import io.katamari.handler.AuthDecoder;
import io.katamari.handler.BodyDecoder;
import io.katamari.handler.HelloWorld;
import io.katamari.handler.UriDecoder;

public class Server {

  public static void main(String [] args) throws Exception {
    new KatamariServer(8080)
      .add(new UriDecoder())
      .add(new BodyDecoder())
      .add(new AuthDecoder("/auth.*", "admin", "secret"))
      .add(new HelloWorld())
      .start();
  }
}