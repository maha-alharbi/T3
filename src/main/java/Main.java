import Actor.Trader;
import Actor.Auditor;
import akka.actor.*;
import Msg.QuoteMsg;
import akka.kafka.javadsl.Consumer;
import FDB.FakeDB;
import kafkaStream.QuoteConsumer;
import kafkaStream.QuoteProducer;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("App started");
        FakeDB.TradersBalance.put(1, 1000);
        ActorSystem akkaSystem = ActorSystem.create("system");
        ActorRef auditorActor = akkaSystem.actorOf(Auditor.props(), "auditorActor");
        ActorRef traderActor = akkaSystem.actorOf(Trader.props(auditorActor), "traderActor");
        QuoteMsg  first = new QuoteMsg("Google", 300, 200, 1);
        QuoteMsg  second = new QuoteMsg("Oracle", 900, 800, 1);
        QuoteMsg  third = new QuoteMsg("Microsoft", 500, 600, 1);
        QuoteMsg  fourth = new QuoteMsg("IBM", 1000, 1100, 1);
        QuoteMsg  fifth = new QuoteMsg("SDAIA", 200, 300, 1);

        QuoteConsumer qConsumer = new QuoteConsumer(traderActor, akkaSystem);

        Consumer.Control consumer = qConsumer.createConsumer();
        akkaSystem.registerOnTermination(consumer::shutdown);
        QuoteProducer qProducer = new QuoteProducer(akkaSystem);

        try {
            Thread.sleep(30000);
            System.out.println("Sending messages by producer !!!!!!");
            qProducer.sendDataViaProducer(first);
            qProducer.sendDataViaProducer(second);
            qProducer.sendDataViaProducer(third);
            qProducer.sendDataViaProducer(fourth);
            qProducer.sendDataViaProducer(fifth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(30000);
        akkaSystem.terminate();




    }
}
