package Actor;

import Msg.Operations;
import Msg.TradeRequest;
import FDB.FakeDB;
import Msg.tradeValidationResponse;
import akka.actor.AbstractActor;
import akka.actor.Props;


public class Auditor extends AbstractActor{


    public static Props props(){
        return Props.create(Auditor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TradeRequest.class,this::validateTradeReq)
                .build();
    }
    private void validateTradeReq(TradeRequest tradeRequest){
        int traderId=tradeRequest.getTraderId();
        int amount=tradeRequest.getAmount();
        int opType = tradeRequest.getOperationType();

        int balance= FakeDB.TradersBalance.get(traderId);

        int  newBalance;

        //if the requested operation is "buy"
        if(opType == 1){
            if(balance >= amount){
                  newBalance = balance - amount;
//                  Operations op = new Operations(1,1,amount,"GoogleTest");
                FakeDB.TradersBalance.replace(traderId,newBalance);
//                FakeDB.OperationsSheet.put(1, op);
                getSender().tell(new tradeValidationResponse(true,"Buy operation confirmed", newBalance),getSelf());
            }
            else{
                getSender().tell(new tradeValidationResponse(false,"Insufficient balance"),getSelf());
            }

        }
        //if the requested operation is "sell"
        else if (opType == 2){
              newBalance = balance + amount;
            FakeDB.TradersBalance.replace(traderId,newBalance);
            getSender().tell(new tradeValidationResponse(true,"Sale operation confirmed",newBalance ),getSelf());
        }

    }

}
