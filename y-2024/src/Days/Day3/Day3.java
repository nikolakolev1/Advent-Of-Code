package Days.Day3;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day3 implements Day {
    private final List<String> input = new ArrayList<>();
    private final Pattern patternMul = Pattern.compile("mul\\([0-9]+,[0-9]+\\)");
    private final Pattern patternDo = Pattern.compile("do\\(\\)");
    private final Pattern patternDont = Pattern.compile("don't\\(\\)");

    public static void main(String[] args) {
        Day day3 = new Day3();
        day3.loadData("data/day3/input.txt");
        System.out.println(day3.part1());
        System.out.println(day3.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        long sum = 0;

        for (String s : input) {
            Matcher matcher = patternMul.matcher(s);

            while (matcher.find()) {
                String match = matcher.group();
                String[] numbers = match.substring(4, match.length() - 1).split(",");
                int result = Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
                sum += result;
            }
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        boolean doFlag = true;
        long sum = 0;

        for (String s : input) {
            Matcher matcherMul = patternMul.matcher(s);
            Matcher matcherDo = patternDo.matcher(s);
            Matcher matcherDont = patternDont.matcher(s);

            while (matcherMul.find()) {
                int doIndex = matcherDo.find() ? matcherDo.start() : -1;
                int dontIndex = matcherDont.find() ? matcherDont.start() : -1;
                int mulIndex = matcherMul.start();

                // "Do" comes first
                if (doIndex != -1 && doIndex < mulIndex && (dontIndex == -1 || doIndex < dontIndex)) {
                    doFlag = true;

                    // Replace the match with an empty string
                    s = s.substring(0, doIndex) + " " + s.substring(doIndex + 4);
                }

                // "Don't" comes first
                else if (dontIndex != -1 && dontIndex < mulIndex && (doIndex == -1 || dontIndex < doIndex)) {
                    doFlag = false;

                    // Replace the match with an empty string
                    s = s.substring(0, dontIndex) + " " + s.substring(dontIndex + 6);
                }

                // "Mul" comes first
                else {
                    if (doFlag) {
                        String match = matcherMul.group();
                        String[] numbers = match.substring(4, match.length() - 1).split(",");
                        int result = Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
                        sum += result;
                    }

                    // Replace the match with an empty string
                    s = s.substring(0, mulIndex) + " " + s.substring(mulIndex + 4);
                }

                // Reset the matchers
                matcherMul = patternMul.matcher(s);
                matcherDo = patternDo.matcher(s);
                matcherDont = patternDont.matcher(s);
            }
        }

        return String.valueOf(sum);
    }
}