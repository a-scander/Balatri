package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Les Jokers qui influent sur la manière d'évaluer les mains:
 Quatre doigts: les suites et couleurs peuvent scorer avec seulement 4 cartes
 Raccourci: Les suites peuvent être formées avec des espacement de *1* entre les cartes */

public class HandEvaluator {

    public static HandResult evaluate(List<Card> cards/*, List<Config> configs*/) {
        if(cards.isEmpty()){return null;}
        boolean isFlush    = checkFlush(cards);
        boolean isStraight = checkStraight(cards);
        if (isFlush && isStraight) return new HandResult(HandType.STRAIGHT_FLUSH, cards);
        if (checkFourOfAKind(cards))  return new HandResult(HandType.FOUR_OF_A_KIND, getFourOfAKindCards(cards));
        if (checkFullHouse(cards))    return new HandResult(HandType.FULL_HOUSE, cards);
        if (isFlush)                  return new HandResult(HandType.FLUSH, cards);
        if (isStraight)               return new HandResult(HandType.STRAIGHT, cards);
        if (checkThreeOfAKind(cards)) return new HandResult(HandType.THREE_OF_A_KIND, getThreeOfAKindCards(cards));
        if (checkTwoPair(cards))      return new HandResult(HandType.TWO_PAIR, getPairCards(cards));
        if (checkPair(cards))         return new HandResult(HandType.PAIR, getPairCards(cards));

        return new HandResult(HandType.HIGH_CARD, List.of(getHighCard(cards)));

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

//get cards active
    private static List<Card> getPairCards(List<Card> cards) {
        return CardUtils.getCardsWithCount(cards, 2);
    }
    
    private static List<Card> getThreeOfAKindCards(List<Card> cards) {
        return CardUtils.getCardsWithCount(cards, 3);
    }
    
    private static List<Card> getFourOfAKindCards(List<Card> cards) {
        return CardUtils.getCardsWithCount(cards, 4);
    }
    
    private static Card getHighCard(List<Card> cards) {
        return CardUtils.getHighestCard(cards);
    }
}