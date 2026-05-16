package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*Quatre doigts: les suites et couleurs peuvent scorer avec seulement 4 cartes
 Raccourci: Les suites  */

public class HandEvaluator {

    public static HandType evaluate(List<Card> cards/*, List<Config> configs*/) {
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
    private static boolean checkFlush(List<Card> cards/*, FlushConfig config */) {
        return CardUtils.groupBySuit(cards).values().stream()
            .anyMatch(list -> list.size() == 5/*config.requiredCardsNumber */);
    }

//Suite
    private static boolean checkStraight(List<Card> cards/*, StraightConfig config */) {
        List<Card> distinctCards = new ArrayList<>(CardUtils.uniqueValues(cards));
        if(distinctCards.size() != 5 /*config.requiredCardNumber */){
            return false;
        }

        Card high = CardUtils.getHighestCard(distinctCards);
        Card low = CardUtils.getLowestCard(distinctCards);

        if(high.isAce() && high.rank().getValue() - low.rank().getValue() != 4){
            distinctCards.removeIf(c -> c.isAce()); 
            Card newHigh = CardUtils.getHighestCard(distinctCards);
            return newHigh.rank().getValue() - low.rank().getValue() == 3; /* 1, 2 - 5 */
        }

        return high.rank().getValue() - low.rank().getValue() == 4;
    }
    
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
        return CardUtils.getRankCounts(cards).containsValue(2L);
    }
}