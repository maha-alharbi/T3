package Msg;

public class QuoteMsg {

    private String companyName;
    private int buyPrice;
    private int sellPrice;

    private int targetedTraderID;

    public QuoteMsg(String companyName, int buyPrice, int sellPrice, int targetedTraderID) {
        this.companyName = companyName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.targetedTraderID = targetedTraderID;
    }

    public QuoteMsg(){

    }

    public String getCompanyName() {
        return companyName;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
    public int getSellPrice() {
        return sellPrice;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public void setBuyPrice(int buyPrice) {

        this.buyPrice = buyPrice;
    }

    public void setSellPrice(int sellPrice) {

        this.sellPrice = sellPrice;
    }

    public void setTargetedTraderID(int targetedTraderID) {

        this.targetedTraderID = targetedTraderID;
    }

    public int getTargetedTraderID() {

       return targetedTraderID;
    }

    public String ToString(){
        return "TraderID = "+this.targetedTraderID +" "+" Company Name = "+this.companyName+ " "+ " Buy Price = "+ this.buyPrice+" "+" Sell Price = "+this.sellPrice;
    }
}
