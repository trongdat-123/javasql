package sample.EDIT;

public class BasicCard extends Card {
    private double fee;

    public BasicCard(String name, String identityCard, String date, String type, double money, double fee) {
        super(name, identityCard, date, type, money);
        this.fee = fee;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) { this.fee = fee;}
    public BasicCard() {}

    @Override
    public double Doanhthu(double money, int month) {
        double m;
        m = money - money * Math.pow((1.0 - 0.5 / 100.0), month);
        if (m > 0) {
            return m;
        }else {
            return money;
        }
    }

    @Override
    public double FEE (double fee){
        return fee * 0.5/100.0;
    }
}

