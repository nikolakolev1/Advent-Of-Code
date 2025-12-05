package Days.Day3;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 implements Day {
    ArrayList<ArrayList<Integer>> batteryBanks = new ArrayList<>();

    public static void main(String[] args) {
        Day day3 = new Day3();
        day3.loadData("data/day3/input_test.txt");
        System.out.println(day3.part1());
        System.out.println(day3.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String batteryBankStr = scanner.nextLine();

                ArrayList<Integer> batteryBank = new ArrayList<>();

                for (int i = 0; i < batteryBankStr.length(); i++) {
                    String digit = String.valueOf(batteryBankStr.charAt(i));
                    batteryBank.add(Integer.parseInt(digit));
                }

                batteryBanks.add(batteryBank);
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (ArrayList<Integer> batteryBank : batteryBanks) {
            int firstBattery = batteryBank.get(0);
            int secondBattery = batteryBank.get(1);

            for (int i = 1; i < batteryBank.size() - 1; i++) {
                int battery = batteryBank.get(i);

                if (battery > firstBattery) {
                    firstBattery = battery;

                    secondBattery = batteryBank.get(i + 1);
                } else if (battery > secondBattery) {
                    secondBattery = battery;
                }
            }

            if (batteryBank.getLast() > secondBattery) secondBattery = batteryBank.getLast();

            sum += Integer.parseInt(firstBattery + "" + secondBattery);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        long sum = 0;

        for (ArrayList<Integer> batteryBank : batteryBanks) {
            ArrayList<Integer> selectedBatteries = new ArrayList<>();
            selectedBatteries.add(batteryBank.get(0));
            selectedBatteries.add(batteryBank.get(1));
            selectedBatteries.add(batteryBank.get(2));
            selectedBatteries.add(batteryBank.get(3));
            selectedBatteries.add(batteryBank.get(4));
            selectedBatteries.add(batteryBank.get(5));
            selectedBatteries.add(batteryBank.get(6));
            selectedBatteries.add(batteryBank.get(7));
            selectedBatteries.add(batteryBank.get(8));
            selectedBatteries.add(batteryBank.get(9));
            selectedBatteries.add(batteryBank.get(10));
            selectedBatteries.add(batteryBank.get(11));

            for (int i = 1; i < batteryBank.size(); i++) {
                int battery = batteryBank.get(i);

                if (i < batteryBank.size() - 11 && battery > selectedBatteries.get(0)) {
                    updateBatteries(0, i, selectedBatteries, batteryBank);
                } else if (i < batteryBank.size() - 10 && battery > selectedBatteries.get(1)) {
                    updateBatteries(1, i, selectedBatteries, batteryBank);
                } else if (i > 1 && i < batteryBank.size() - 9 && battery > selectedBatteries.get(2)) {
                    updateBatteries(2, i, selectedBatteries, batteryBank);
                } else if (i > 2 && i < batteryBank.size() - 8 && battery > selectedBatteries.get(3)) {
                    updateBatteries(3, i, selectedBatteries, batteryBank);
                } else if (i > 3 && i < batteryBank.size() - 7 && battery > selectedBatteries.get(4)) {
                    updateBatteries(4, i, selectedBatteries, batteryBank);
                } else if (i > 4 && i < batteryBank.size() - 6 && battery > selectedBatteries.get(5)) {
                    updateBatteries(5, i, selectedBatteries, batteryBank);
                } else if (i > 5 && i < batteryBank.size() - 5 && battery > selectedBatteries.get(6)) {
                    updateBatteries(6, i, selectedBatteries, batteryBank);
                } else if (i > 6 && i < batteryBank.size() - 4 && battery > selectedBatteries.get(7)) {
                    updateBatteries(7, i, selectedBatteries, batteryBank);
                } else if (i > 7 && i < batteryBank.size() - 3 && battery > selectedBatteries.get(8)) {
                    updateBatteries(8, i, selectedBatteries, batteryBank);
                } else if (i > 8 && i < batteryBank.size() - 2 && battery > selectedBatteries.get(9)) {
                    updateBatteries(9, i, selectedBatteries, batteryBank);
                } else if (i > 9 && i < batteryBank.size() - 1 && battery > selectedBatteries.get(10)) {
                    updateBatteries(10, i, selectedBatteries, batteryBank);
                } else if (i > 10 && battery > selectedBatteries.get(11)) {
                    updateBatteries(11, i, selectedBatteries, batteryBank);
                }
            }

            System.out.println("" + selectedBatteries.get(0) + selectedBatteries.get(1) + selectedBatteries.get(2) + selectedBatteries.get(3) + selectedBatteries.get(4) + selectedBatteries.get(5) + selectedBatteries.get(6) + selectedBatteries.get(7) + selectedBatteries.get(8) + selectedBatteries.get(9) + selectedBatteries.get(10) + selectedBatteries.get(11));
            sum += Long.parseLong("" + selectedBatteries.get(0) + selectedBatteries.get(1) + selectedBatteries.get(2) + selectedBatteries.get(3) + selectedBatteries.get(4) + selectedBatteries.get(5) + selectedBatteries.get(6) + selectedBatteries.get(7) + selectedBatteries.get(8) + selectedBatteries.get(9) + selectedBatteries.get(10) + selectedBatteries.get(11));
        }

        return String.valueOf(sum);
    }

    private static void updateBatteries(int updateFromIndex, int newlySelectedBatteryIndex, ArrayList<Integer> selectedBatteries, ArrayList<Integer> batteryBank) {
        int steps = 0;
        for (int i = updateFromIndex; i < selectedBatteries.size(); i++) {
            selectedBatteries.set(i, batteryBank.get(newlySelectedBatteryIndex + steps));
            steps++;
        }
    }
}