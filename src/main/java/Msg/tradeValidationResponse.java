package Msg;

public class tradeValidationResponse {

        boolean result;
        String text;

        int remainingBalance;

        public tradeValidationResponse(boolean result, String text, int remainingBalance) {
            this.result = result;
            this.text = text;
            this.remainingBalance = remainingBalance;
        }


    public tradeValidationResponse(boolean result, String text) {
        this.result = result;
        this.text = text;

    }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


    public void setRemainingBalance(int newBalance){
    this.remainingBalance = newBalance;
    }

    public int getRemainingBalance(){
            return remainingBalance;
    }

}

