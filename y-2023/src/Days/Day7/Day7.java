package Days.Day7;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day7 implements Day {
    private record hand_bid(int[] hand, int bid) {
    }

    private record card_count(int card, int count) {
    }

    private static final HashMap<Character, Integer> mapCardToInt = new HashMap<>(), mapCardToInt_P2 = new HashMap<>();
    private List<hand_bid> hands = new ArrayList<>();
    private final List<ArrayList<hand_bid>> handsByCombination = new ArrayList<>(), handsByCombination_P2 = new ArrayList<>();
    private static final Comparator<int[]> myComparator = Day7::compareHands_SameCombination;
    private static final int handI = 0, bidI = 1; // for parsing the data
    private static final int handLength = 5; // hardcoded
    private static final int highCard = 0, onePair = 1, twoPairs = 2, threeOfAKind = 3, fullHouse = 4, fourOfAKind = 5, fiveOfAKind = 6;

    public static void main(String[] args) {
        Day day7 = new Day7();
        day7.loadData(Helper.filename(7));
        System.out.println(day7.part1());
        System.out.println(day7.part2());
    }

    @Override
    public void loadData(String filename) {
        initialize();

        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(" ");

                int[] hand = new int[handLength];
                int[] hand_P2 = new int[handLength];

                for (int i = 0; i < split[handI].length(); i++) {
                    char card = split[handI].charAt(i);

                    hand[i] = mapCardToInt.get(card);
                    hand_P2[i] = mapCardToInt_P2.get(card);
                }

                int bid = Integer.parseInt(split[bidI]);

                hand_bid hand_bid = new hand_bid(hand, bid);
                hand_bid hand_bid_P2 = new hand_bid(hand_P2, bid);

                int combination = classifyHand(hand);
                int combination_P2 = classifyHand_P2(hand_P2);

                handsByCombination.get(combination).add(hand_bid);
                handsByCombination_P2.get(combination_P2).add(hand_bid_P2);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public long part1() {
        for (ArrayList<hand_bid> handsList_combination : handsByCombination) {
            handsList_combination.sort((o1, o2) -> myComparator.compare(o1.hand, o2.hand));

            hands.addAll(handsList_combination);
        }

        return calcResult();
    }

    @Override
    public long part2() {
        hands = new ArrayList<>();

        for (ArrayList<hand_bid> hands_combination : handsByCombination_P2) {
            hands_combination.sort((o1, o2) -> myComparator.compare(o1.hand, o2.hand));

            hands.addAll(hands_combination);
        }

        return calcResult();
    }

    private void initialize() {
        initHM();
        initHM_P2();
        initAL();
    }

    private void initHM() {
        mapCardToInt.put('2', 2);
        mapCardToInt.put('3', 3);
        mapCardToInt.put('4', 4);
        mapCardToInt.put('5', 5);
        mapCardToInt.put('6', 6);
        mapCardToInt.put('7', 7);
        mapCardToInt.put('8', 8);
        mapCardToInt.put('9', 9);
        mapCardToInt.put('T', 10);
        mapCardToInt.put('J', 11);
        mapCardToInt.put('Q', 12);
        mapCardToInt.put('K', 13);
        mapCardToInt.put('A', 14);
    }

    private void initHM_P2() {
        mapCardToInt_P2.put('2', 2);
        mapCardToInt_P2.put('3', 3);
        mapCardToInt_P2.put('4', 4);
        mapCardToInt_P2.put('5', 5);
        mapCardToInt_P2.put('6', 6);
        mapCardToInt_P2.put('7', 7);
        mapCardToInt_P2.put('8', 8);
        mapCardToInt_P2.put('9', 9);
        mapCardToInt_P2.put('T', 10);
        mapCardToInt_P2.put('J', 1);
        mapCardToInt_P2.put('Q', 12);
        mapCardToInt_P2.put('K', 13);
        mapCardToInt_P2.put('A', 14);
    }

    private void initAL() {
        for (int i = 0; i < 7; i++) {
            handsByCombination.add(new ArrayList<>());
            handsByCombination_P2.add(new ArrayList<>());
        }
    }

    private static int compareHands_SameCombination(int[] hand1, int[] hand2) {
        for (int i = 0; i < handLength; i++) {
            if (hand1[i] > hand2[i]) return 1;
            else if (hand1[i] < hand2[i]) return -1;
        }

        return 0;
    }

    private int classifyHand(int[] hand) {
        List<card_count> cardCounts = countCardsInHand(hand);

        List<Integer> counts = new ArrayList<>();
        for (card_count card_count : cardCounts) {
            counts.add(card_count.count);
        }

        switch (counts.size()) {
            case 1 -> {
                return fiveOfAKind;
            }
            case 2 -> {
                if (counts.contains(4)) return fourOfAKind;
                else return fullHouse;
            }
            case 3 -> {
                if (counts.contains(3)) return threeOfAKind;
                else return twoPairs;
            }
            case 4 -> {
                return onePair;
            }
            case 5 -> {
                return highCard;
            }
            default -> throw new RuntimeException("Shouldn't be reached");
        }
    }

    private int classifyHand_P2(int[] hand) {
        List<card_count> cardCounts = countCardsInHand(hand);

        List<Integer> counts = new ArrayList<>();
        for (card_count card_count : cardCounts) {
            if (card_count.card != 1) counts.add(card_count.count);
        }

        int wildcards = countXInHand(hand, 1);

        // edge case
        if (wildcards == 5) return fiveOfAKind;

        int xOfAKind = counts.stream()
                .reduce(0, Math::max);

        switch (xOfAKind) {
            case 5 -> {
                return fiveOfAKind;
            }
            case 4 -> {
                if (wildcards == 1) return fiveOfAKind;
                else return fourOfAKind;
            }
            case 3 -> {
                if (wildcards == 2) return fiveOfAKind;
                else if (wildcards == 1) return fourOfAKind;
                else if (counts.size() == 2) return fullHouse;
                else return threeOfAKind;
            }
            case 2 -> {
                if (wildcards == 3) return fiveOfAKind;
                else if (wildcards == 2) return fourOfAKind;
                else if (wildcards == 1) {
                    if (counts.size() == 2) return fullHouse;
                    else return threeOfAKind;
                } else if (counts.size() == 3) return twoPairs;
                else return onePair;
            }
            case 1 -> {
                if (wildcards == 4) return fiveOfAKind;
                else if (wildcards == 3) return fourOfAKind;
                else if (wildcards == 2) return threeOfAKind;
                else if (wildcards == 1) return onePair;
                else return highCard;
            }
            default -> throw new RuntimeException("Shouldn't be reached");
        }
    }

    private List<card_count> countCardsInHand(int[] hand) {
        List<card_count> cardCounts = new ArrayList<>();

        for (int card : hand) {
            if (!cardListContainsX(cardCounts, card)) {
                cardCounts.add(new card_count(card, countXInHand(hand, card)));
            }
        }

        return cardCounts;
    }

    private boolean cardListContainsX(List<card_count> cardList, int x) {
        for (card_count card_count : cardList) {
            if (card_count.card == x) return true;
        }

        return false;
    }

    private int countXInHand(int[] hand, int card) {
        return (int) Arrays.stream(hand)
                .filter(i -> i == card)
                .count();
    }

    private long calcResult() {
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bid;
        }
        return sum;
    }
}