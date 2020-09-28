package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import com.codenotfound.jms.Receiver;
import com.codenotfound.jms.ReceiverConfig;
import com.codenotfound.jms.Sender;

import org.apache.activemq.artemis.junit.EmbeddedJMSResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static java.lang.Math.random;
import static java.lang.Math.round;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude=ReceiverConfig.class)
public class SpringJmsApplicationTest {

  @Rule
  public EmbeddedJMSResource resource = new EmbeddedJMSResource();

  @Autowired
  private Sender sender;

  @Autowired
  private Receiver receiver;

  @Test
  public void testSend() throws Exception {
    for (int i = 0; i < 10; i++) {
      sender.send("Hello Spring JMS ActiveMQ!");
    }
  }

  @Test
  public void testReceive() throws Exception {
    sender.send("Hello Spring JMS ActiveMQ!");

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }

  @Test
  public void artemisIsFaster() {
    long t1 = System.currentTimeMillis();

    for (int i = 1; i <= 1_000_000; i++) {

      JsonObject json = Json.createObjectBuilder().add("windrad", 6).add("kw/h", 33).build();

      String msg = json.toString();

      String key = String.valueOf(round(random() * 1000));
      double value = new Double(round(random() * 10000000L)).intValue() / 1000.0;
      
      sender.send(key + msg);
    }
    System.out.println("Zeit: " + ((System.currentTimeMillis() - t1) / 1000f) + " Sek.");
  }
}
