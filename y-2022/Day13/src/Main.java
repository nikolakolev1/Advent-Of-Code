import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<Packet> packets = new ArrayList<>();
    private static final ArrayList<Integer> indicesOfPairsInOrder = new ArrayList<>();

    public static void main(String[] args) {
        loadData();

        part1();
        System.out.println();
        part2();
    }

    private static void loadData() {
        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisLine = myScanner.nextLine();

                if (!thisLine.isEmpty()) {
                    packets.add(new Packet(thisLine));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 2022 version
    private static boolean comparePackets(Packet left, Packet right) {
        int combinedLength = left.items.size() + right.items.size() + 1;
        boolean bool;

        Item leftCurrentItem, rightCurrentItem;

        for (int i = 0; i < combinedLength; i++) {
            if (left.items.size() == i) return true;
            leftCurrentItem = left.items.get(i);

            if (right.items.size() == i) return false;
            rightCurrentItem = right.items.get(i);

            if (leftCurrentItem.isInteger && rightCurrentItem.isInteger) {
                if (leftCurrentItem.intValue < rightCurrentItem.intValue) {
                    return true;
                } else if (leftCurrentItem.intValue > rightCurrentItem.intValue) {
                    return false;
                }
            } else {
                bool = comparePackets(leftCurrentItem.returnItemAsPacket(), rightCurrentItem.returnItemAsPacket());
                return bool;
            }
        }

        // debug
//        System.out.println("This shouldn't be reached... maybe... idk now");
        return true;
    }

    // 2023 version
    private static int comparePacketsNew(Packet left, Packet right) {
        int combinedLength = left.items.size() + right.items.size() + 1;

        Item leftCurrentItem, rightCurrentItem;

        for (int i = 0; i < combinedLength; i++) {
            if (left.items.size() == i) return 1;
            leftCurrentItem = left.items.get(i);

            if (right.items.size() == i) return -1;
            rightCurrentItem = right.items.get(i);

            if (leftCurrentItem.isInteger && rightCurrentItem.isInteger) {
                int compareInts = compareInts(leftCurrentItem.intValue, rightCurrentItem.intValue);
                if (compareInts != 0) return compareInts;
            } else if (leftCurrentItem.isInteger && rightCurrentItem.isList) {
                int compareIntAndList = compareIntAndList(leftCurrentItem.intValue, rightCurrentItem);
                if (compareIntAndList != 0) return compareIntAndList;
            } else if (leftCurrentItem.isList && rightCurrentItem.isInteger) {
                int compareListAndInt = compareListAndInt(leftCurrentItem, rightCurrentItem.intValue);
                if (compareListAndInt != 0) return compareListAndInt;
            } else {
                int compareLists = compareLists(leftCurrentItem, rightCurrentItem);
                if (compareLists != 0) return compareLists;
            }
        }

        return 1;
    }

    private static int compareInts(int left, int right) {
        return Integer.compare(right, left);
    }

    private static int compareLists(Item leftList, Item rightList) {
        int combinedLength = leftList.list.size() + rightList.list.size() + 1;

        Item leftCurrentItem, rightCurrentItem;

        for (int i = 0; i < combinedLength; i++) {
            if (leftList.list.size() == i) return 1;
            leftCurrentItem = leftList.list.get(i);

            if (rightList.list.size() == i) return -1;
            rightCurrentItem = rightList.list.get(i);

            if (leftCurrentItem.isInteger && rightCurrentItem.isInteger) {
                int compareInts = compareInts(leftCurrentItem.intValue, rightCurrentItem.intValue);
                if (compareInts != 0) return compareInts;
            } else if (leftCurrentItem.isInteger && rightCurrentItem.isList) {
                int compareIntAndList = compareIntAndList(leftCurrentItem.intValue, rightCurrentItem);
                if (compareIntAndList != 0) return compareIntAndList;
            } else if (leftCurrentItem.isList && rightCurrentItem.isInteger) {
                int compareListAndInt = compareListAndInt(leftCurrentItem, rightCurrentItem.intValue);
                if (compareListAndInt != 0) return compareListAndInt;
            } else {
                int compareLists = compareLists(leftCurrentItem, rightCurrentItem);
                if (compareLists != 0) return compareLists;
            }
        }

        return 1;
    }

    private static int compareIntAndList(int left, Item right) {
        String leftAsListString = "[" + left + "]";
        Item leftAsList = new Item(leftAsListString);

        return compareLists(leftAsList, right);
    }

    private static int compareListAndInt(Item left, int right) {
        String rightAsListString = "[" + right + "]";
        Item rightAsList = new Item(rightAsListString);

        return compareLists(left, rightAsList);
    }

    private static void part1() {
        int packetsSize = packets.size();
        for (int i = 0; i < packetsSize; i++) {
            if (comparePackets(packets.get(i), packets.get(i + 1))) {
                indicesOfPairsInOrder.add((i + 2) / 2);
            }
            i++;
        }

        int result = 0;
        for (Integer pairIndex : indicesOfPairsInOrder) {
            result += pairIndex;
        }
        System.out.println("=== Part 1 ===\nAnswer: " + result);
    }

    private static void part2() {
        packets.add(new Packet("[[2]]"));
        packets.add(new Packet("[[6]]"));

        // 2022 version
//        ArrayList<Packet> sortedPackets = mergesort(packets);

        // 2023 version
        packets.sort((left, right) -> -1 * comparePacketsNew(left, right));

        ArrayList<Integer> part2Answer = new ArrayList<>();

        for (int i = 0; i < packets.size(); i++) {
            // debug
//            String index = i + "";
//            if (i < 10) index += "  ";
//            else if (i < 100) index += " ";
//            System.out.println(index + ": " + packets.get(i).packetAsString);

            if (packets.get(i).packetAsString.equals("[[2]]") || packets.get(i).packetAsString.equals("[[6]]")) {
                part2Answer.add(i);
            }
        }

        System.out.println("===  Part 2 ===\nAnswer: " + (part2Answer.get(0) + 1) * (part2Answer.get(1) + 1));
    }

    private static ArrayList<Packet> merge(ArrayList<Packet> a, ArrayList<Packet> b) {
        ArrayList<Packet> merged = new ArrayList<>();
        int mergedEndSize = a.size() + b.size();
        int indexA = 0, indexB = 0; // indexA, indexB: next index to be checked from a & b

        // put the smaller numbers into merged
        for (int i = 0; i < mergedEndSize; i++) {
            if (indexB >= b.size() || (indexA < a.size() && (comparePackets(a.get(indexA), b.get(indexB))))) {
                merged.add(a.get(indexA++)); // if the packet in  a-arrL is smaller
            } else {
                merged.add(b.get(indexB++)); // if the packet in b-arrL is smaller
            }
        }

        return merged;
    }

    private static ArrayList<Packet> mergesort(ArrayList<Packet> packets) {
        if (packets.size() < 2) return packets; // if arr is too short -> return it

        ArrayList<Packet> arrL1 = new ArrayList<>(), arrL2 = new ArrayList<>();

        int half = packets.size() / 2;
        for (int i = 0; i < half; i++) {
            arrL1.add(packets.get(i));
        }
        for (int i = half; i < packets.size(); i++) {
            arrL2.add(packets.get(i));
        }

        return merge(mergesort(arrL1), mergesort(arrL2)); // merge and return
    }
}

class Packet {
    ArrayList<Item> items = new ArrayList<>();
    String packetAsString;

    Packet(String thisLine) {
        packetAsString = thisLine;

        packetAsString_Manipulation(packetAsString);
    }

    void packetAsString_Manipulation(String packetAsString) {
        String_Manipulator.packetOrItemAsString_Manipulation(packetAsString, items);
    }
}

class Item {
    Integer intValue;
    ArrayList<Item> list = new ArrayList<>();
    String listAsString;
    boolean isInteger, isList;

    Item(int i) {
        intValue = i;
        isInteger = true;
    }

    Item(String list) {
        listAsString = list;
        isList = true;

        listAsString_Manipulation(listAsString);
    }

    void listAsString_Manipulation(String listAsString) {
        String_Manipulator.packetOrItemAsString_Manipulation(listAsString, list);
    }

    Packet returnItemAsPacket() {
        if (isList) {
            return new Packet(listAsString);
        } else {
            return new Packet("[" + intValue + "]");
        }
    }
}

class String_Manipulator {
    public static void packetOrItemAsString_Manipulation(String packetOrListStr, ArrayList<Item> packetOrList) {
        int openingBracketsCounter = 0;
        int closingBracketsCounter = 0;

        for (int i = 0; i < packetOrListStr.length(); i++) {
            char currentChar = packetOrListStr.charAt(i);

            if (Character.isDigit(currentChar)) {
                if (Character.isDigit(packetOrListStr.charAt(i + 1))) {
                    packetOrList.add(new Item(Integer.parseInt(packetOrListStr.substring(i, i + 2))));
                    i++;
                } else {
                    packetOrList.add(new Item(Integer.parseInt(packetOrListStr.substring(i, i + 1))));
                }
            } else if (currentChar == 91) { // char is [
                openingBracketsCounter++;
                if (openingBracketsCounter > 1) {
                    int tempOpenCounter = 0;
                    int tempCloseCounter = 0;
                    for (int j = i; j < packetOrListStr.length(); j++) {
                        currentChar = packetOrListStr.charAt(j);
                        if (currentChar == 91) {
                            tempOpenCounter++;
                        } else if (currentChar == 93) {
                            tempCloseCounter++;
                            if (tempOpenCounter == tempCloseCounter) {
                                packetOrList.add(new Item(packetOrListStr.substring(i, j + 1)));
                                i = j - 1;
                                break;
                            }
                        }
                    }
                }
            } else if (currentChar == 93) { // char is ]
                if (++closingBracketsCounter == openingBracketsCounter) {
                    return;
                }
            }
        }
    }
}