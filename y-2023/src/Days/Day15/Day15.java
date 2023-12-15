package Days.Day15;

import General.Day;
import General.Helper;

import java.io.File;
import java.util.*;

public class Day15 implements Day {
    List<char[]> input = new ArrayList<>();
    List<List<Lens>> boxes = new ArrayList<>();

    private record Lens(char[] label, int focalLength) {
    }

    public static void main(String[] args) {
        Day day15 = new Day15();
        day15.loadData(Helper.filename(15));
        System.out.println(day15.part1());
        System.out.println(day15.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                Arrays.stream(scanner.nextLine().split(",")).forEach(array -> this.input.add(array.toCharArray()));
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        return String.valueOf(input.stream().mapToInt(this::hash).sum());
    }

    @Override
    public String part2() {
        initBoxes();

        for (char[] arr : input) {
            // remove the given lens from the given box
            if (contains(arr, '-')) {
                remove(arr);
            }

            // add the given lens to the given box
            else {
                add(arr);
            }
        }

        return String.valueOf(focusingPower());
    }

    // Apply the hash function to the char[] (part 1) the label of the lens (part 2)
    private int hash(char[] in) {
        int hash = 0;
        for (char c : in) {
            hash = ((hash + c) * 17) % 256;
        }
        return hash;
    }

    // Returns true if the char[] contains the given char
    private boolean contains(char[] in, char c) {
        for (char ch : in) {
            if (ch == c) return true;
        }
        return false;
    }

    // Returns true if the box contains a lens with the given label
    private boolean contains(List<Lens> box, char[] label) {
        for (Lens lens : box) {
            if (Arrays.equals(lens.label, label)) return true;
        }
        return false;
    }

    // Returns the index of the given char in the given char[]
    private int indexOf(char[] in, char c) {
        for (int i = 0; i < in.length; i++) {
            if (in[i] == c) return i;
        }
        return -1;
    }

    // Remove the lens with the given label from the given box
    private void remove(char[] in) {
        // in = abc-
        char[] label = Arrays.copyOfRange(in, 0, in.length - 1);

        Iterator<Lens> iterator = boxes.get(hash(label)).iterator();
        while (iterator.hasNext()) {
            Lens lens = iterator.next();
            if (Arrays.equals(lens.label, label)) {
                iterator.remove();
                break;
            }
        }
    }

    // Add the lens with the given label and focal length to the given box
    private void add(char[] in) {
        // in = abc=1
        int split = indexOf(in, '=');

        char[] label = Arrays.copyOfRange(in, 0, split);
        char[] fl = Arrays.copyOfRange(in, split + 1, in.length);

        List<Lens> box = boxes.get(hash(label));
        Lens lens = new Lens(label, Integer.parseInt(String.valueOf(fl)));

        if (!contains(box, label)) {
            box.add(lens);
        } else {
            replace(box, lens);
        }
    }

    // Replace the lens with the given label in the given box with the given lens
    private void replace(List<Lens> box, Lens lens) {
        for (int i = 0; i < box.size(); i++) {
            if (Arrays.equals(box.get(i).label, lens.label)) {
                box.set(i, lens);
                break;
            }
        }
    }

    // Initialize the boxes
    private void initBoxes() {
        for (int i = 0; i <= 255; i++) {
            boxes.add(new ArrayList<>());
        }
    }

    // Calculate the focusing power
    private int focusingPower() {
        int power = 0;

        for (int boxNo = 0; boxNo < boxes.size(); boxNo++) {
            List<Lens> box = boxes.get(boxNo);
            for (int slotNo = 0; slotNo < box.size(); slotNo++) {
                Lens lens = box.get(slotNo);

                power += (boxNo + 1) * (slotNo + 1) * lens.focalLength;
            }
        }

        return power;
    }
}