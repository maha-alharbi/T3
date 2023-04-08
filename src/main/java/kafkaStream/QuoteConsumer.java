package kafkaStream;

import Msg.QuoteMsg;
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.kafka.javadsl.Committer;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.actor.ActorSystem;
import akka.kafka.*;
import akka.kafka.javadsl.Consumer;
import akka.stream.javadsl.Sink;
import com.typesafe.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;


public class QuoteConsumer {

   private ActorRef trader;
   private  ActorSystem system;
   private  Config config;
   private  String topicName;
    private ConsumerSettings<String, byte[]> consumerSettings;

//    private  Materializer materializer;
//    private CommitterSettings committerSettings;


        public QuoteConsumer (ActorRef traderAct,ActorSystem akkaSystem){

        this.trader = traderAct;
        this.system = akkaSystem;
        this.config = system.settings().config().getConfig("akka.kafka.consumer");
        this.topicName = config.getString("topic-name");
        this.consumerSettings = ConsumerSettings.create(config, new StringDeserializer(), new ByteArrayDeserializer());

//        this.materializer= ActorMaterializer.create(system);
//        this.committerSettings = CommitterSettings.create(system);
    }
    ObjectMapper mapper = new ObjectMapper();
    ObjectReader reader = mapper.readerFor(QuoteMsg.class);




    public Consumer.Control createConsumer (){
        Consumer.Control control =
                Consumer.plainSource(consumerSettings, Subscriptions.topics(topicName))
                        .map(record -> record.value())
                        .map(value -> {
                            QuoteMsg quoteMsg = reader.readValue(value, QuoteMsg.class);
                            System.out.println("Quote msg received by consumer: " +quoteMsg.ToString() );
                            return quoteMsg;
                        })
                        .to(Sink.foreach(quoteMsg -> trader.tell(quoteMsg, ActorRef.noSender())))
                        .run(system);

        return  control;
    }


//    public Consumer.Control createNewConsumer() {
//        Consumer.Control control = Consumer.committableSource(consumerSettings,
//                        Subscriptions.topics("marketData"))
//
//                .map(record -> record.record().value())
//                .map(value -> {
//                    QuoteMsg quoteMsg = reader.readValue(value, QuoteMsg.class);
//                    System.out.println("Quote msg received by consumer: " + quoteMsg.ToString());
//                    return quoteMsg;
//                })
////                .<ConsumerMessage.Committable>thenApply(done -> record().value().committableOffset()))
//                .to(Sink.foreach(quoteMsg -> trader.tell(quoteMsg, ActorRef.noSender())))
////                .toMat(Committer.sink(committerSettings.withMaxBatch(1)))
////                .mapMaterializedValue(Committer.sink(committerSettings), Consumer::createDrainingControl)
//
//
//
////                  .runWith(Sink.foreach(quoteMsg -> trader.tell(quoteMsg, ActorRef.noSender())), materializer);
////                 .runWith(system);
//
//                .run(materializer);
//
//
//        return control;
//
//    }


}


