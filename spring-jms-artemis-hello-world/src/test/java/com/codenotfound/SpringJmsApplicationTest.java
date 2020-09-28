package com.codenotfound;

import static java.lang.Math.random;
import static java.lang.Math.round;

import javax.json.Json;
import javax.json.JsonObject;

import com.codenotfound.jms.Sender;

import org.apache.activemq.artemis.junit.EmbeddedJMSResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringJmsApplicationTest {

  @Rule
  public EmbeddedJMSResource resource = new EmbeddedJMSResource();

  @Autowired
  private Sender sender;

  @Test
  public void artemisIsFaster() {
    long t1 = System.currentTimeMillis();

    JsonObject json = Json.createObjectBuilder().add("windrad", 6).add("kw/h", 33).build();

      String msg = json.toString();

    for (int i = 1; i <= 1_000_000; i++) {
      String key = String.valueOf(round(random() * 1000));
      double value = new Double(round(random() * 10000000L)).intValue() / 1000.0;
      
      sender.send(key + msg);
    }
    System.out.println("Zeit: " + ((System.currentTimeMillis() - t1) / 1000f) + " Sek.");
  }
}
