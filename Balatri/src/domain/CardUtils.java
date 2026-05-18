package domain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
Class made to handle list<Card> related functions
 */
public class CardUtils {

    // [4H, 4D, 9S, 13c, 2H] -> "Two of Hearts, Four of Hearts, Four of Diamonds, Nine of Spades, King of Clubs"
    public static String cardsToStringBig(List<Card> cards) {
        sort(cards);
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(", "));
    }

    // [4H, 4D, 9S, 13c, 2H] -> "2H, 4H, 4D, 9S, 13c"
    public static String cardsToString(List<Card> cards) {
        sort(cards);
        return cards.stream()
                .map(Card::toStringSmall)
                .collect(Collectors.joining(", "));
    }

    // [4H, 4D, 9S, 13c, 2H] -> [2H, 4H, 4D, 9S, 13c]
    public static void sort(List<Card> cards) {
        cards.sort(Comparator.comparingInt(card -> card.rank().getValue()));
    }

    // [4H, 4D, 9S, 13c, 2H] -> {2=[TWO of HEARTS], 4=[FOUR of HEARTS, FOUR of DIAMONDS], 9=[NINE of SPADES], 13=[KING of CLUBS]}
    public static Map<Integer, List<Card>> groupByValue(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(card -> card.rank().getValue()));
    }

    // [4H, 4D, 9S, 13c, 2H] -> {DIAMONDS=[FOUR of DIAMONDS], CLUBS=[KING of CLUBS], HEARTS=[TWO of HEARTS, FOUR of HEARTS], SPADES=[NINE of SPADES]}
    public static Map<Suit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::suit));
    }

    // [4H, 4D, 9S, 13c, 2H] -> KING of CLUBS
    public static Card getHighestCard(List<Card> cards) {
        return cards.stream()
                .max(Comparator.comparingInt(card -> card.rank().getValue()))
                .orElse(null);
    }

    // [4H, 4D, 9S, 13c, 2H] -> TWO of HEARTS
    public static Card getLowestCard(List<Card> cards) {
        return cards.stream()
                .min(Comparator.comparingInt(card -> card.rank().getValue()))
                .orElse(null);
    }

    // [4H, 4D, 9S, 13c, 2H] -> {2=1, 4=2, 9=1, 13=1}
    public static Map<Integer, Long> getRankCounts(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(card -> card.rank().getValue(), Collectors.counting()));
    }

    // [4H, 4D, 9S, 13c, 2H], value=4 -> 2
    public static long countOf(List<Card> cards, int value) {
        return getRankCounts(cards).getOrDefault(value, 0L);
    }

    // [4H, 4D, 9S, 13c, 2H], n=2 -> true  |  n=3 -> false
    public static boolean hasOfAKind(List<Card> cards, int n) {
        return getRankCounts(cards).values().stream().anyMatch(v -> v == n);
    }

    // [4H, 4D, 9S, 13c, 2H], size=2 -> 1  |  size=1 -> 3
    public static long numberOfGroups(List<Card> cards, int size) {
        return getRankCounts(cards).values().stream().filter(v -> v == size).count();
    }

    // [4H, 4D, 9S, 13c, 2H], count=2 -> 4  |  count=1 -> 13
    public static int getHighestValueWithCount(List<Card> cards, int count) {
        return getRankCounts(cards).entrySet().stream()
                .filter(e -> e.getValue() == count)
                .map(Map.Entry::getKey)
                .max(Integer::compareTo)
                .orElse(-1);
    }

    // [4H, 4D, 9S, 13c, 2H], count=1 -> [2, 9, 13]  |  count=2 -> [4]
    public static List<Integer> getValuesWithCount(List<Card> cards, int count) {
        return getRankCounts(cards).entrySet().stream()
                .filter(e -> e.getValue() == count)
                .map(Map.Entry::getKey)
                .toList();
    }

   // [4H, 4D, 9S, 13C, 2H] -> [2H, 4H, 9S, 13C]  
    public static List<Card> uniqueValues(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.toMap(
                    card -> card.rank().getValue(),
                    card -> card,
                    (a, b) -> a
                ))
                .values().stream()
                .toList();
    }
}