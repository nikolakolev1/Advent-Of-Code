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
            System.out.println("File not found exception occurred");
        }
    }

    // compare two packets
    private static int compare(Packet leftPacket, Packet rightPacket) {
        int length = leftPacket.items.size() + rightPacket.items.size() + 1;
        Item left, right;

        for (int i = 0; i < length; i++) {
            if (leftPacket.items.size() == i) return 1;
            left = leftPacket.items.get(i);

            if (rightPacket.items.size() == i) return -1;
            right = rightPacket.items.get(i);

            int compare;
            if (left.isInteger && right.isInteger) {
                compare = compare(left.intValue, right.intValue);
            } else if (left.isInteger && right.isList) {
                compare = compare(left.intValue, right);
            } else if (left.isList && right.isInteger) {
                compare = compare(left, right.intValue);
            } else {
                compare = compare(left, right);
            }

            if (compare != 0) return compare;
        }

        // if we get here, the two packets are equal
        return 1;
    }

    // compare two integers
    private static int compare(int left, int right) {
        return Integer.compare(right, left);
    }

    // compare two lists
    private static int compare(Item leftList, Item rightList) {
        int length = leftList.list.size() + rightList.list.size() + 1;
        Item left, right;

        for (int i = 0; i < length; i++) {
            if (leftList.list.size() == i) return 1;
            left = leftList.list.get(i);

            if (rightList.list.size() == i) return -1;
            right = rightList.list.get(i);

            int compare;
            if (left.isInteger && right.isInteger) {
                compare = compare(left.intValue, right.intValue);
            } else if (left.isInteger && right.isList) {
                compare = compare(left.intValue, right);
            } else if (left.isList && right.isInteger) {
                compare = compare(left, right.intValue);
            } else {
                compare = compare(left, right);
            }

            if (compare != 0) return compare;
        }

        // if we get here, the two lists are equal
        return 1;
    }

    // compare an integer and a list
    private static int compare(int left, Item right) {
        Item leftAsList = new Item("[" + left + "]");

        return compare(leftAsList, right);
    }

    // compare a list and an integer
    private static int compare(Item left, int right) {
        Item rightAsList = new Item("[" + right + "]");

        return compare(left, rightAsList);
    }

    private static void part1() {
        int packetsSize = packets.size();
        for (int i = 0; i < packetsSize; i += 2) {
            if (compare(packets.get(i), packets.get(i + 1)) != -1) {
                indicesOfPairsInOrder.add((i + 2) / 2);
            }
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

        packets.sort((left, right) -> -1 * compare(left, right));

        ArrayList<Integer> part2Answer = new ArrayList<>();

        for (int i = 0; i < packets.size(); i++) {
            // debug
            // printPackage(i);

            if (packets.get(i).packetAsString.equals("[[2]]") || packets.get(i).packetAsString.equals("[[6]]")) {
                part2Answer.add(i);
            }
        }

        System.out.println("===  Part 2 ===\nAnswer: " + (part2Answer.get(0) + 1) * (part2Answer.get(1) + 1));
    }

    // debug
    private static void printPackage(int i) {
        String index = i + "";
        if (i < 10) index += "  ";
        else if (i < 100) index += " ";
        System.out.println(index + ": " + packets.get(i).packetAsString);
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
        String_Manipulator.manipulate(packetAsString, items);
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
        String_Manipulator.manipulate(listAsString, list);
    }
}

class String_Manipulator {
    public static void manipulate(String packetOrListStr, ArrayList<Item> packetOrList) {
        int openBracketsCount = 0;
        int closeBracketsCount = 0;

        for (int i = 0; i < packetOrListStr.length(); i++) {
            char currentChar = packetOrListStr.charAt(i);

            if (Character.isDigit(currentChar)) {
                int start = i;
                if (Character.isDigit(packetOrListStr.charAt(i + 1))) i++;
                int end = i + 1;

                packetOrList.add(new Item(Integer.parseInt(packetOrListStr.substring(start, end))));
            } else if (currentChar == 91) { // char is [
                if (++openBracketsCount > 1) {
                    int innerOpenCount = 0;
                    int innerCloseCount = 0;

                    for (int j = i; j < packetOrListStr.length(); j++) {
                        currentChar = packetOrListStr.charAt(j);

                        if (currentChar == 91) { // char is [
                            innerOpenCount++;
                        } else if (currentChar == 93) { // char is ]
                            innerCloseCount++;

                            if (innerOpenCount == innerCloseCount) {
                                packetOrList.add(new Item(packetOrListStr.substring(i, j + 1)));
                                i = j - 1;
                                break;
                            }
                        }
                    }
                }
            } else if (currentChar == 93) { // char is ]
                if (++closeBracketsCount == openBracketsCount) {
                    return;
                }
            }
        }
    }
}