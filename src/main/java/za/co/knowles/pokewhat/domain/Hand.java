package za.co.knowles.pokewhat.domain;

import za.co.knowles.pokewhat.domain.lookup.ERank;
import za.co.knowles.pokewhat.domain.lookup.ESuit;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cards = new ArrayList<>(5);

    public Hand() {
    }

    public Hand(Card one, Card two, Card three, Card four, Card five) {
        cards.add(one);
        cards.add(two);
        cards.add(three);
        cards.add(four);
        cards.add(five);
    }

    public ERank getCardRank(int index) {
        return cards.get(index).getRank();
    }

    public void addCard(ERank rank, ESuit suit) {
        cards.add(new Card(suit, rank));
    }

    public long toBitField() {
        return 1 << cardRank(0) | 1 << cardRank(1) | 1 << cardRank(2) | 1 << cardRank(3) | 1 << cardRank(4);
    }

    private int cardRank(int i) {
        return cards.get(i).getRank().getNumeric();
    }

    public Set<ESuit> getSuitSet() {
        return cards.stream().map(Card::getSuit).collect(Collectors.toSet());
    }

    public Map<ERank, Long> getRankCount() {
        return cards.stream()
                .map(Card::getRank)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    public boolean isComplete() {
        return cards.size() == 5;
    }

    public void add(Card card) {
        this.cards.add(card);
    }

    public void sortCards() {
        this.cards.sort(Comparator.comparingInt(c -> -c.getRank().getNumeric()));
    }

    public List<Card> getCards() {
        return cards;
    }
}
