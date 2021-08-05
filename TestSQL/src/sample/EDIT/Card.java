package sample.EDIT;

public class Card {

    private String name, identityCard, date, type;
    private double money;

    public Card(String name, String identityCard, String date,String type, double money) {
        this.name = name;
        this.identityCard = identityCard;
        this.date = date;
        this.money = money;
        this.type = type;
    }

    public Card(){}
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) { this.identityCard = identityCard; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) { this.money = money; }

    //Thiết lập 2 phương thức tính phí duy trì và doạnh thu//
    public double Doanhthu(double money, int month){ return money;}

    public double FEE(double fee){
        return fee;
    }
}