package Actor;

import Msg.QuoteMsg;
import Msg.TradeRequest;
import Msg.tradeValidationResponse;
import akka.actor.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TypedTraderAct extends AbstractBehavior<QuoteMsg> {


    private ActorRef auditor;


    //1- Matching constructor :
    public TypedTraderAct(ActorContext<QuoteMsg> context) {
        super(context);
    }



    //2- Behavior (Props replacement) :
    public static Behavior<QuoteMsg> create(){
        return Behaviors.setup(TypedTraderAct::new);
    }

    //3- override receive, return newReceiveBuilder, .onMessage instead of .match
    @Override
    public Receive<QuoteMsg> createReceive() {
        return newReceiveBuilder()
//                .onMessage(QuoteMsg.class, this::onQuote)
                .build();
    }


    private void onQuote(QuoteMsg quote) throws InterruptedException, TimeoutException {
        int buyPrice = quote.getBuyPrice();
        int sellPrice = quote.getSellPrice();
        int traderID = quote.getTargetedTraderID();
        String companyName = quote.getCompanyName();
        TradeRequest tradeRequest;

        //if the buy price is greater than the sell price we will make a "buy" operation, by sending trade request to the Auditor Actor
        if (buyPrice > sellPrice) {

            tradeRequest = new TradeRequest(traderID, sellPrice,1 );
            makeTrade(tradeRequest, companyName);

            //if the buy price is less than the sell price we will make a "sale" operation
        } else if (buyPrice < sellPrice) {

            tradeRequest = new TradeRequest(traderID, sellPrice,2 );
            makeTrade(tradeRequest, companyName);

        }
    }

    private void makeTrade(TradeRequest tradeRequest, String companyName ) throws InterruptedException, TimeoutException {



        FiniteDuration timeOut = Duration.create(10, TimeUnit.SECONDS);
        Future<Object> ask = Patterns.ask(auditor, tradeRequest, timeOut.toMillis());

        tradeValidationResponse response = (tradeValidationResponse) Await.result(ask,timeOut);

        boolean isTradeConfirmed=response.isResult();
        String  description=response.getText();
        int remainingBalance = response.getRemainingBalance();

        if(isTradeConfirmed==true){
            System.out.println("Trade confirmed for "+ companyName +" company " + response.getText() +" The remaining balance is "+ remainingBalance);
        }
        else{
            System.out.println("Trade rejected for "+ companyName +" company " +description);
        }
    }


}




