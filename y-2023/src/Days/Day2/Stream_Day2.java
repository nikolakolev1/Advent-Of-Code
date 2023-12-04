package Days.Day2;

import General.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Stream_Day2 {
    private static final ArrayList<int[]> data = new ArrayList<>();
    private static final int RED_I = 0, GREEN_I = 1, BLUE_I = 2, GAME_I = 3;
    private static final int RED_MAX = 12, GREEN_MAX = 13, BLUE_MAX = 14;

    public static void main(String[] args) {
        loadData();
        System.out.println(part1());
        System.out.println(part2());
    }

    public static void loadData() {
        try {
            File input = new File(Helper.filename(2));
            Scanner scanner = new Scanner(input);

            int game = 1;

            while (scanner.hasNextLine()) {
                int[] rgbValues = new int[4];
                rgbValues[GAME_I] = game++;

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
        // for each game, check if the max value for each color > the max possible value
        return data.stream()
                .filter(rgbValues -> rgbValues[RED_I] <= RED_MAX && rgbValues[GREEN_I] <= GREEN_MAX && rgbValues[BLUE_I] <= BLUE_MAX)
                .mapToInt(rgbValues -> rgbValues[GAME_I])
                .sum();
    }

    public static int part2() {
        // for each game, add the product of the max values for each color
        return data.stream()
                .mapToInt(rgbValues -> rgbValues[RED_I] * rgbValues[GREEN_I] * rgbValues[BLUE_I])
                .sum();
    }
}