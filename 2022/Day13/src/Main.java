import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Packet> packets = new ArrayList<>();
    public static ArrayList<Integer> indicesOfPairsInOrder = new ArrayList<>();

    public static void main(String[] args) {
        loadData();

        mainMethod();
        System.out.println("debug");
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
            } else if (leftCurrentItem.isList && rightCurrentItem.isList) {
                bool = comparePackets(new Packet(leftCurrentItem.listAsString), new Packet(rightCurrentItem.listAsString));
                return bool;
            } else {
                if (leftCurrentItem.isInteger) {
                    bool = comparePackets(leftCurrentItem.returnItemAsPacket(), new Packet(rightCurrentItem.listAsString));
                } else {
                    bool = comparePackets(new Packet(leftCurrentItem.listAsString), rightCurrentItem.returnItemAsPacket());
                }
                return bool;
            }
        }

//        System.out.println("This shouldn't be reached... maybe... idk now");
        return true;
    }

    private static void mainMethod() {
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
        System.out.println(result);
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