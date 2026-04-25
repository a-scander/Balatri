package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandEvaluator {

    public static HandType evaluate(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Une main doit contenir exactement 5 cartes");
        }

        boolean isFlush    = checkFlush(cards);
        boolean isStraight = checkStraight(cards);

        if (isFlush && isStraight) return HandType.STRAIGHT_FLUSH;
        if (checkFourOfAKind(cards))  return HandType.FOUR_OF_A_KIND;
        if (checkFullHouse(cards))    return HandType.FULL_HOUSE;
        if (isFlush)                  return HandType.FLUSH;
        if (isStraight)               return HandType.STRAIGHT;
        if (checkThreeOfAKind(cards)) return HandType.THREE_OF_A_KIND;
        if (checkTwoPair(cards))      return HandType.TWO_PAIR;
        if (checkPair(cards))         return HandType.PAIR;

        return HandType.HIGH_CARD;
    }
//Couleur
    private static boolean checkFlush(List<Card> cards) {
        Suit first = cards.get(0).suit();
        
        for (Card c : cards) {
            if (c.suit() != first) {
                return false; 
            }
        }
        return true; 
    }
//Suite
    private static boolean checkStraight(List<Card> cards) {
        List<Integer> values = new ArrayList<>();

        for (Card c : cards) {
            values.add(c.rank().getValue());
        }

        Collections.sort(values);


        boolean normal = true;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != values.get(i - 1) + 1) {
                normal = false;
                break;
            }
        }
        if (normal) return true;

        if (values.equals(List.of(2, 3, 4, 5, 14))) return true;

        return false;
    }
    
//Number rank
    private static Map<Rank, Integer> getRankCounts(List<Card> cards) {
        Map<Rank, Integer> counts = new HashMap<>();

        for (Card card : cards) {
            Rank rank = card.rank();
            
   
            counts.put(rank, counts.getOrDefault(rank, 0) + 1);
        }

        return counts;
    }
    
    //Carre
    private static boolean checkFourOfAKind(List<Card> cards) {
        return getRankCounts(cards).containsValue(4);
    }

    //Full 
    private static boolean checkFullHouse(List<Card> cards) {
        Map<Rank, Integer> counts = getRankCounts(cards);
        return counts.containsValue(3) && counts.containsValue(2);
    }
//Brelan
    private static boolean checkThreeOfAKind(List<Card> cards) {
        return getRankCounts(cards).containsValue(3);
    }
//Double paire
    private static boolean checkTwoPair(List<Card> cards) {
        int pairCount = 0;

        for (Integer count : getRankCounts(cards).values()) {
            
            if (count == 2) { 
                pairCount++;
            }
        }
        return pairCount == 2;
    }
    
//Paire
    private static boolean checkPair(List<Card> cards) {
        return getRankCounts(cards).containsValue(2);
    }
}