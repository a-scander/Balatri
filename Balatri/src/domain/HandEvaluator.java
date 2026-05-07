package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Quatre doigts: les suites et couleurs peuvent scorer avec seulement 4 cartes
 Raccourci: Les suites  */

public class HandEvaluator {

    public static HandType evaluate(List<Card> cards/*, List<Config> configs*/) {
        boolean isFlush    = checkFlush(cards);
        boolean isStraight = checkStraightBetter(cards);

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
        return CardUtils.groupBySuit(cards).keySet().size() == 1;
        /*
        Suit first = cards.get(0).suit();
        
        for (Card c : cards) {
            if (c.suit() != first) {
                return false; 
            }
        }

        return true; */
    }
//Suite
    private static boolean checkStraightBetter(List<Card> cards/*, StraightConfig config */) {
        List<Card> distinctCards = CardUtils.sortedUniqueValues(cards);
        
        if(distinctCards.size() != 5 /*config.requiredCardNumber */){
            return false;
        }

        Card high = CardUtils.getHighestCard(distinctCards);
        Card low = CardUtils.getLowestCard(distinctCards);
        if(high.isAce() && high.rank().getValue() - low.rank().getValue() != 4){
            distinctCards.remove(high);
            Card newHigh = CardUtils.getHighestCard(distinctCards);
            return newHigh.rank().getValue() - low.rank().getValue() == 3; /* 1, 2 - 5 */
        }

        return high.rank().getValue() - low.rank().getValue() == 4;
    }

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
    // placed in CardsUtils
    /*private static Map<Rank, Integer> getRankCounts(List<Card> cards) {
        Map<Rank, Integer> counts = new HashMap<>();

        for (Card card : cards) {
            Rank rank = card.rank();
            
   
            counts.put(rank, counts.getOrDefault(rank, 0) + 1);
        }

        return counts;
    }*/
    
    //Carre
    private static boolean checkFourOfAKind(List<Card> cards) {
        return CardUtils.getRankCounts(cards).containsValue(4L);
    }

    //Full 
    private static boolean checkFullHouse(List<Card> cards) {
        Map<Integer, Long> counts = CardUtils.getRankCounts(cards);
        return counts.containsValue(3L) && counts.containsValue(2L);
    }
//Brelan
    private static boolean checkThreeOfAKind(List<Card> cards) {
        return CardUtils.getRankCounts(cards).containsValue(3L);
    }
//Double paire
    private static boolean checkTwoPair(List<Card> cards) {
        int pairCount = 0;

        for (Long count : CardUtils.getRankCounts(cards).values()) {
            
            if (count == 2) { 
                pairCount++;

                if(pairCount == 2) return true;
            }
        }

        return false;
        
    }
    
//Paire
    private static boolean checkPair(List<Card> cards) {
        return CardUtils.getRankCounts(cards).containsValue(2);
    }
}