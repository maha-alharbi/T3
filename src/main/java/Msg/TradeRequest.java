package Msg;

public class TradeRequest {



        private int traderId;
        private int amount;

        private int operationType; //1 for buy , and 2 for sale

        // Constructors

        public TradeRequest(){}

        public TradeRequest(int traderId,int amount, int opType){
            this.traderId=traderId;
            this.amount=amount;
            this.operationType=opType;
        }

        // Getter  and setters
        public int getTraderId() {
            return traderId;
        }

        public void setTraderId(int traderId) {
            this.traderId = traderId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getOperationType() { return operationType;}

    public void setOperationType(int opType){
        this.operationType = opType;
    }
    }


