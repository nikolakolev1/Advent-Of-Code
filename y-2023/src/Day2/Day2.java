package Day2;

import Helpers.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day2 {
    private static final ArrayList<int[]> data = new ArrayList<>();
    private static final int RED_I = 0, GREEN_I = 1, BLUE_I = 2;
    private static final int RED_MAX = 12, GREEN_MAX = 13, BLUE_MAX = 14;

    public static void main(String[] args) {
        loadData(Helper.filename(2));
        System.out.println(part1());
        System.out.println(part2());
    }

    public static void loadData(String filename) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                int[] rgbValues = new int[3];

                String line = scanner.nextLine();
                String[] split = line.split(" ");

                // store the max value for each color
                for (int i = 3; i < split.length; i += 2) {
                    if (split[i].startsWith("red")) {
                        rgbValues[RED_I] = Math.max(Integer.parseInt(split[i - 1]), rgbValues[RED_I]);
                    } else if (split[i].startsWith("green")) {
                        rgbValues[GREEN_I] = Math.max(Integer.parseInt(split[i - 1]), rgbValues[GREEN_I]);
                    } else if (split[i].startsWith("blue")) {
                        rgbValues[BLUE_I] = Math.max(Integer.parseInt(split[i - 1]), rgbValues[BLUE_I]);
                    } else {
                        throw new Exception("Invalid color: " + split[i]);
                    }
                }

                data.add(rgbValues);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public static int part1() {
        int dataSize = data.size(); // avoid calling .size() every iteration

        int sum = 0;

        // for each game, check if the max value for each color > the max possible value
        for (int i = 0; i < dataSize; i++) {
            if (data.get(i)[RED_I] <= RED_MAX && data.get(i)[GREEN_I] <= GREEN_MAX && data.get(i)[BLUE_I] <= BLUE_MAX) {
                sum += (i + 1);
            }
        }

        return sum;
    }

    public static int part2() {
        int sum = 0;

        // for each game, add the product of the max values for each color
        for (int[] rgbValues : data) {
            sum += (rgbValues[RED_I] * rgbValues[GREEN_I] * rgbValues[BLUE_I]);
        }

        return sum;
    }
}