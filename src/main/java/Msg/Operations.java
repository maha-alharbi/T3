package Msg;

public class Operations {

    private int traderID;
    private int opType;
    private int cost;
    private String companyName;



    public Operations() {

    }
    public Operations(int traderID, int opType, int cost, String companyName) {
        this.traderID = traderID;
        this.opType = opType;
        this.cost = cost;
        this.companyName = companyName;
    }

    public void setTraderID(int traderID) {
        this.traderID = traderID;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getTraderID() {
        return traderID;
    }

    public int getOpType() {
        return opType;
    }

    public int getCost() {
        return cost;
    }

    public String getCompanyName() {
        return companyName;
    }

   public String toString(){
        String opTypeStr;
        if(this.opType == 1){
            opTypeStr = "buy";
        }else{
            opTypeStr="sale";
        }
        return "Trader id = "+this.traderID+" "+" Operation type = "+opTypeStr + " "+ "Company Name = "+ this.companyName +" "+" Cost = "+this.cost;
    }
}
