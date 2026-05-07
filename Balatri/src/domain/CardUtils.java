package domain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
Class made to handle list<Card> related functions
 */
public class CardUtils {
    public static String cardsToStringBig(List<Card> cards) {
        sort(cards);
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(", "));
    }

    public static String cardsToString(List<Card> cards) {
        sort(cards);
        return cards.stream()
                .map(Card::toStringSmall)
                .collect(Collectors.joining(", "));
    }

    public static void sort(List<Card> cards){
        cards.sort(Comparator.comparingInt(card -> card.rank().getValue()));
    }

    public static Map<Integer, List<Card>> groupByValue(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(card -> card.rank().getValue()));
    }

    public static Map<Suit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::suit));
    }

    public static Card getHighestCard(List<Card> cards) {
        return cards.stream()
                .max(Comparator.comparingInt(card -> card.rank().getValue()))
                .orElse(null);
    }

    public static Card getLowestCard(List<Card> cards) {
        return cards.stream()
                .min(Comparator.comparingInt(card -> card.rank().getValue()))
                .orElse(null);
    }

    public static Map<Integer, Long> getRankCounts(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(card -> card.rank().getValue(), Collectors.counting()));
    }

    public static long countOf(List<Card> cards, int value) {
        return getRankCounts(cards).getOrDefault(value, 0L);
    }

    public static boolean hasOfAKind(List<Card> cards, int n) {
        return getRankCounts(cards).values().stream().anyMatch(v -> v == n);
    }

    public static long numberOfGroups(List<Card> cards, int size) {
        return getRankCounts(cards).values().stream().filter(v -> v == size).count();
    }

    public static int getHighestValueWithCount(List<Card> cards, int count) {
        return getRankCounts(cards).entrySet().stream()
                .filter(e -> e.getValue() == count)
                .map(Map.Entry::getKey)
                .max(Integer::compareTo)
                .orElse(-1);
    }

    public static List<Integer> getValuesWithCount(List<Card> cards, int count) {
        return getRankCounts(cards).entrySet().stream()
                .filter(e -> e.getValue() == count)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static List<Card> sortedUniqueValues(List<Card> cards) {
        return cards.stream()
                .distinct()
                .sorted(Comparator.comparingInt(card -> card.rank().getValue()))
                .toList();
    }
}