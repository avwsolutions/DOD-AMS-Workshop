package com.devoteam.datalake;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.HdrHistogram.Histogram;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

/**
 * THis program reads out the graphite topic
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
        // initialize objectmapper for JSON
        ObjectMapper mapper = new ObjectMapper();

        // Initialize the consumer
        KafkaConsumer<String, String> consumer;
        try (InputStream props = Resources.getResource("consumer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }
        consumer.subscribe(Arrays.asList("metrics"));
        int timeouts = 0;
        //noinspection InfiniteLoopStatement
        while (true) {
            // read records with a short timeout. If we time out, we don't really care.
            ConsumerRecords<String, String> records = consumer.poll(600);
            if (records.count() == 0) {
                timeouts++;
            } else {
                System.out.printf("Got %d records after %d timeouts\n", records.count(), timeouts);
                timeouts = 0;
            }
            for (ConsumerRecord<String, String> record : records) {
                switch (record.topic()) {
                    case "metrics":
                    	// first parse out the metricname,value and timestamp
                    	
                    	JsonNode metric = mapper.readTree(record.value());
                    	String name = metric.get("name").asText();
                    	int value = metric.get("value").asInt();
                    	long timestamp = metric.get("timestamp").asLong();
                    	
                        // the send time is encoded inside the message
                    	SimpleGraphiteClient graphiteClient = new SimpleGraphiteClient("localhost", 2003);
                    	System.out.println("sending" + name + "," + value + "," + timestamp);
                    	graphiteClient.sendMetric(name, value, timestamp);
                   // default:
                   //     throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                }
            }
        }
    }
}
