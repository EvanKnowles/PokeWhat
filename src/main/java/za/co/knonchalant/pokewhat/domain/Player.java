package za.co.knonchalant.pokewhat.domain;

public class Player {
    private final String name;
    private double money;

    public Player(String name, double money) {
        this.name = name;
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return name;
    }

    public void loseMoney(double bet) {
        money -= bet;
    }
}
