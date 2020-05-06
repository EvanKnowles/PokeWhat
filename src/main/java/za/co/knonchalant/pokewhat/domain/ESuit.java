package za.co.knonchalant.pokewhat.domain;

public enum ESuit {
    SPADES("♠"), CLUBS("♣"), HEARTS("♥"), DIAMONDS("♦");

    private String picture;

    ESuit(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public Card the(ERank rank) {
        return new Card(this, rank);
    }
}
