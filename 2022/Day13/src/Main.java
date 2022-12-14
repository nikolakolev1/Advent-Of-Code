import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Packet> packets = new ArrayList<>();
    public static ArrayList<Integer> indicesOfPairsInOrder = new ArrayList<>();

    public static void main(String[] args) {
        loadData();

        part1();
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

//        System.out.println("This shouldn't be reached... maybe... idk now");
        return true;
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
        System.out.println("Part 1: " + result);
    }

    private static void part2() {
        packets.add(new Packet("[[2]]"));
        packets.add(new Packet("[[6]]"));

        ArrayList<Packet> sortedPackets = mergesort(packets);
        ArrayList<Integer> part2Answer = new ArrayList<>();

        for (int i = 0; i < sortedPackets.size(); i++) {
//            System.out.println(sortedPackets.get(i).packetAsString);
            if (sortedPackets.get(i).packetAsString.equals("[[2]]") || sortedPackets.get(i).packetAsString.equals("[[6]]")) {
                part2Answer.add(i);
            }
        }

        System.out.println("(incorrect) Part 2: " + part2Answer.get(0) * part2Answer.get(1));
    }

    public static ArrayList<Packet> merge(ArrayList<Packet> a, ArrayList<Packet> b) {
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

    /*
     * mergesort method for Task 1.2
     */
    public static ArrayList<Packet> mergesort(ArrayList<Packet> packets) {
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
    int openingBracketsCounter = 0;
    int closingBracketsCounter = 0;

    Packet(String thisLine) {
        packetAsString = thisLine;

        packetAsString_Manipulation(packetAsString);
    }

    // TODO: Fix repetition (almost the same method in Item)
    void packetAsString_Manipulation(String packetAsString) {
        for (int i = 0; i < packetAsString.length(); i++) {
            char currentChar = packetAsString.charAt(i);

            if (Character.isDigit(currentChar)) {
                if (Character.isDigit(packetAsString.charAt(i + 1))) {
                    items.add(new Item(Integer.parseInt(packetAsString.substring(i, i + 2))));
                    i++;
                } else {
                    items.add(new Item(Integer.parseInt(packetAsString.substring(i, i + 1))));
                }
            } else if (currentChar == 91) { // is '['
                openingBracketsCounter++;
                if (openingBracketsCounter > 1) {
                    int tempOpenCounter = 0;
                    int tempCloseCounter = 0;
                    for (int j = i; j < packetAsString.length(); j++) {
                        currentChar = packetAsString.charAt(j);
                        if (currentChar == 91) {
                            tempOpenCounter++;
                        } else if (currentChar == 93) {
                            tempCloseCounter++;
                            if (tempOpenCounter == tempCloseCounter) {
                                items.add(new Item(packetAsString.substring(i, j + 1)));
                                i = j - 1;
                                break;
                            }
                        }
                    }
                }
            } else if (currentChar == 93) { // is ']'
                if (++closingBracketsCounter == openingBracketsCounter) {
                    return;
                }
            }
        }
    }
}

class Item {
    Integer intValue;
    ArrayList<Item> list = new ArrayList<>();
    String listAsString;
    boolean isInteger = false;
    boolean isList = false;
    int openingBracketsCounter = 0;
    int closingBracketsCounter = 0;

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
        for (int i = 0; i < listAsString.length(); i++) {
            char currentChar = listAsString.charAt(i);

            if (Character.isDigit(currentChar)) {
                if (Character.isDigit(listAsString.charAt(i + 1))) {
                    list.add(new Item(Integer.parseInt(listAsString.substring(i, i + 2))));
                    i++;
                } else {
                    list.add(new Item(Integer.parseInt(listAsString.substring(i, i + 1))));
                }
            } else if (currentChar == 91) { // is '['
                openingBracketsCounter++;
                if (openingBracketsCounter > 1) {
                    int tempOpenCounter = 0;
                    int tempCloseCounter = 0;
                    for (int j = i; j < listAsString.length(); j++) {
                        currentChar = listAsString.charAt(j);
                        if (currentChar == 91) {
                            tempOpenCounter++;
                        } else if (currentChar == 93) {
                            tempCloseCounter++;
                            if (tempOpenCounter == tempCloseCounter) {
                                list.add(new Item(listAsString.substring(i, j + 1)));
                                i = j - 1;
                                break;
                            }
                        }
                    }
                }
            } else if (currentChar == 93) { // is ']'
                if (++closingBracketsCounter == openingBracketsCounter) {
                    return;
                }
            }
        }
    }

    Packet returnItemAsPacket() {
        if (isList) {
            return new Packet(listAsString);
        } else {
            return new Packet("[" + intValue + "]");
        }
    }
}