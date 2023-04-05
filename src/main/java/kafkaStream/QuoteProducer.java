package kafkaStream;


import Msg.QuoteMsg;

import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;

import akka.Done;
import akka.actor.ActorSystem;
import akka.kafka.*;
import akka.kafka.javadsl.Consumer;
import akka.kafka.javadsl.Producer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.typesafe.config.Config;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QuoteProducer {

    private ActorSystem system;
    private Config config;

    private String topicName;

    private Sink<ProducerRecord<String, String>, CompletionStage<Done>> sink;

    private ProducerSettings<String, String> producerSettings;

    public QuoteProducer(ActorSystem akkaSystem) {
        this.system = akkaSystem;
        this.config = system.settings().config().getConfig("akka.kafka.producer");
        this.topicName = config.getString("topic-name");
        this.producerSettings = ProducerSettings.create(config, new StringSerializer(), new StringSerializer())
                .withBootstrapServers("localhost:9092");

        this.sink = Producer.plainSink(producerSettings);
    }

//    private static final ActorSystem system = ActorSystem.create();


    ObjectMapper mapper = new ObjectMapper();

    public void sendDataViaProducer(QuoteMsg quote) throws Exception {

//        LinkedList quoteList = new LinkedList<>();
//        quoteList.add(quote);

        System.out.println("Topic Name : " + topicName);
        // #plainSink

        CompletionStage<Done> done =
                Source.single(quote)
                        .map(msg -> {
                            String jsonValue = mapper.writeValueAsString(msg);
                            System.out.println("Json msg : " + jsonValue);
                            return jsonValue;
                        })
                        .map(value -> new ProducerRecord<String, String>(topicName, value))
                        .runWith(sink, system);
        done.whenComplete(new BiConsumer<Done, Throwable>() {
            @Override
            public void accept(Done done, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("Done ");
                } else {
                    throwable.printStackTrace();
                }

            }
        });
    }


}
