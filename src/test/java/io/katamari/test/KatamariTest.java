package io.katamari.test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import io.katamari.Server;
import io.katamari.ServerPipeline;
import io.katamari.handler.InboundMessageHandler;
import io.katamari.settings.Settings;
import io.netty.channel.ChannelPipeline;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class KatamariTest {

  private Map<String, InboundMessageHandler> handlers = new LinkedHashMap<String, InboundMessageHandler>();
  private Server server;

  protected AsyncHttpClient httpClient = new AsyncHttpClient();
  protected Settings.SettingsBuilder settingsBuilder = new Settings.SettingsBuilder().put("http.port", "8080");

  @Before
  public void startServer() throws Exception {
    configure();
    server = new Server(settingsBuilder.build(), new ServerPipeline() {
      public void populate(ChannelPipeline pipeline) {
        for (Map.Entry<String, InboundMessageHandler> entry : handlers.entrySet()) {
          pipeline.addLast(entry.getKey(), entry.getValue());
        }
      }
    });

    server.start();
    server.waitForStart();
  }

  @After
  public void stopServer() {
    server.shutdown();
  }

  public abstract void configure();

  public void addHandler(String name, InboundMessageHandler inboundMessageHandler) {
    handlers.put(name, inboundMessageHandler);
  }

  public Response sendRequest(Request request) throws IOException, ExecutionException, InterruptedException {
    Response response = httpClient.prepareRequest(request).execute().get();
    assertThat(response, is(notNullValue()));
    return response;
  }

  protected RequestBuilder GET() {
    return new RequestBuilder().setMethod("GET").setUrl("http://localhost:8080/");
  }

  protected void assertIsForbidden(Response response) {
    assertThat(response.getStatusCode(), is(403));
  }

  protected void assertIsOk(Response response) {
    assertThat(response.getStatusCode(), is(200));
  }


}
