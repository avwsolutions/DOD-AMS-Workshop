package com.devoteam.datalake;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

/**
 * This producer will send a bunch of messages to topic "fast-messages". Every so often,
 * it will send a message to "slow-messages". This shows how messages can be sent to
 * multiple topics. On the receiving end, we will see both kinds of messages but will
 * also see how the two topics aren't really synchronized.
 */
public class Producer {
    public static void main(String[] args) throws IOException {
    	System.out.println("set up the producer");
        // set up the producer
        KafkaProducer<String, String> producer;
        try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }
        try {
        	for(int x = 0; x < 25; x = x+1) {
      	      
   	         	Path path = Paths.get("/tmp/events.txt");
   	         	Random uniq = new Random();
   	         
   	         	DateTime dt = DateTime.now();
   	         	dt = dt.minusHours(x);
   	         	DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
   	         	String tvalue = fmt.print(dt);
   	         	//   System.out.println(str);
   	         
   	         	try (Stream<String> lines = Files.lines(path)) {
   	         		lines.forEach(
   	         				s -> { System.out.println("{\"timestamp\":\"" + tvalue + "\", " + "\"message\":\"" + s + "\", " + "\"message_id\":\"" + uniq.nextInt(10000) + "\"}");
   	         					   producer.send(new ProducerRecord<String, String>(
   	         							   "events",
   	         							   String.format("{\"timestamp\":\"%s\", \"message\":\"%s\", \"message_id\":\"%d\"}", tvalue , s, uniq.nextInt(10000))));
   	         					           //String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
   	         					           producer.flush();
   	         				}
   	         		)	;
   	         	} catch (IOException ex) {

   	         	}
        	 }
        	
        } catch (Throwable throwable) {
            System.out.printf("%s", throwable.getStackTrace());
        } finally {
            producer.close();
        }

    }
}
