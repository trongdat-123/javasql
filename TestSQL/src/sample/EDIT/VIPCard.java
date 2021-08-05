package sample.EDIT;

public class VIPCard extends Card {
    private double fee;

    public VIPCard(String name, String identityCard, String date, String type, double money, double fee) {
        super(name, identityCard, date, type, money);
        this.fee = fee;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public VIPCard() {}

    @Override
    public double Doanhthu(double money, int month) {
        double m;
        m = money - money * Math.pow((1.0 - 1.0 / 100.0), month) + 20000.0 * (1.0 - Math.pow(0.99, month)) / 0.01;
        if (m > 0) {
            return m;
        }else {
            return money;
        }
    }

    @Override
    public double FEE(double fee){
        return fee * 1.0/100.0 + 20000.0;
    }
}
