package Actor;

import FDB.FakeDB;
import Msg.TradeRequest;
import Msg.tradeValidationResponse;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.io.dns.internal.Message;
import akka.japi.pf.ReceiveBuilder;
import akka.actor.*;
import Msg.QuoteMsg;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Trader extends AbstractActor {


    private ActorRef auditor;

    public static Props props(ActorRef Auditor) {

        return Props.create(Trader.class, Auditor);
    }

    public Trader() {
    }

    public Trader(ActorRef Auditor) {
        this.auditor = Auditor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(QuoteMsg.class, this::onQuote)
                .build();
    }


    private void onQuote(QuoteMsg quote) throws InterruptedException, TimeoutException {
        int buyPrice = quote.getBuyPrice();
        int sellPrice = quote.getSellPrice();
        int traderID = quote.getTargetedTraderID();
        String companyName = quote.getCompanyName();
        TradeRequest tradeRequest;

        //if the buy price is greater than the sell price we will make a "buy" operation, by sending trade request to the Auditor Actor
        if (buyPrice >= sellPrice) {

            tradeRequest = new TradeRequest(traderID, sellPrice, 1);
            makeTrade(tradeRequest, companyName);

            //if the buy price is less than the sell price we will make a "sale" operation
        } else if (buyPrice < sellPrice) {

            tradeRequest = new TradeRequest(traderID, sellPrice, 2);
            makeTrade(tradeRequest, companyName);

        }
    }

    private void makeTrade(TradeRequest tradeRequest, String companyName) throws InterruptedException, TimeoutException {


        //Set waiting time
        FiniteDuration timeOut = Duration.create(10, TimeUnit.SECONDS);
        //Ask return Future object which represents possible reply
        Future<Object> ask = Patterns.ask(auditor, tradeRequest, timeOut.toMillis());

        tradeValidationResponse response = (tradeValidationResponse) Await.result(ask, timeOut);

        boolean isTradeConfirmed = response.isResult();
        String description = response.getText();
        int remainingBalance = response.getRemainingBalance();

        if (isTradeConfirmed == true) {
            System.out.println("Trade confirmed for " + companyName + " company " + response.getText() + " The remaining balance is " + remainingBalance);
        } else {
            System.out.println("Trade rejected for " + companyName + " company " + description);
        }
    }


}





