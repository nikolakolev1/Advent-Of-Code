package Days.Day3;

import General.Day;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 implements Day {
    ArrayList<ArrayList<Battery>> batteryBanks = new ArrayList<>();

    public static void main(String[] args) {
        Day day3 = new Day3();
        day3.loadData("data/day3/input.txt");
        System.out.println(day3.part1());
        System.out.println(day3.part2());
    }

    private static void updateBatteries(int updateFromIndex, int newlySelectedBatteryIndex, ArrayList<Battery> selectedBatteries, ArrayList<Battery> batteryBank) {
        int steps = 0;
        for (int i = updateFromIndex; i < selectedBatteries.size(); i++) {
            selectedBatteries.set(i, batteryBank.get(newlySelectedBatteryIndex + steps));
            steps++;
        }
    }

    private static ArrayList<Battery> getInitialBatteries(ArrayList<Battery> batteryBank) {
        ArrayList<Battery> selectedBatteries = new ArrayList<>();

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

        return selectedBatteries;
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String batteryBankStr = scanner.nextLine();

                ArrayList<Battery> batteryBank = new ArrayList<>();

                for (int i = 0; i < batteryBankStr.length(); i++) {
                    String digit = String.valueOf(batteryBankStr.charAt(i));
                    batteryBank.add(new Battery(Integer.parseInt(digit), i));
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

        for (ArrayList<Battery> batteryBank : batteryBanks) {
            int firstBattery = batteryBank.get(0).value;
            int secondBattery = batteryBank.get(1).value;

            for (int i = 1; i < batteryBank.size() - 1; i++) {
                int battery = batteryBank.get(i).value;

                if (battery > firstBattery) {
                    firstBattery = battery;

                    secondBattery = batteryBank.get(i + 1).value;
                } else if (battery > secondBattery) {
                    secondBattery = battery;
                }
            }

            secondBattery = Math.max(secondBattery, batteryBank.getLast().value);

            sum += Integer.parseInt(firstBattery + "" + secondBattery);
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2() {
        long sum = 0;

        for (ArrayList<Battery> batteryBank : batteryBanks) {
            ArrayList<Battery> selectedBatteries = getInitialBatteries(batteryBank);

            for (int i = 1; i < batteryBank.size(); i++) {
                Battery battery = batteryBank.get(i);

                if (i < batteryBank.size() - 11 && battery.value > selectedBatteries.get(0).value) {
                    updateBatteries(0, i, selectedBatteries, batteryBank);
                } else if (i < batteryBank.size() - 10 && battery.index > selectedBatteries.get(0).index && battery.value > selectedBatteries.get(1).value) {
                    updateBatteries(1, i, selectedBatteries, batteryBank);
                } else if (i > 1 && i < batteryBank.size() - 9 && battery.index > selectedBatteries.get(1).index && battery.value > selectedBatteries.get(2).value) {
                    updateBatteries(2, i, selectedBatteries, batteryBank);
                } else if (i > 2 && i < batteryBank.size() - 8 && battery.index > selectedBatteries.get(2).index && battery.value > selectedBatteries.get(3).value) {
                    updateBatteries(3, i, selectedBatteries, batteryBank);
                } else if (i > 3 && i < batteryBank.size() - 7 && battery.index > selectedBatteries.get(3).index && battery.value > selectedBatteries.get(4).value) {
                    updateBatteries(4, i, selectedBatteries, batteryBank);
                } else if (i > 4 && i < batteryBank.size() - 6 && battery.index > selectedBatteries.get(4).index && battery.value > selectedBatteries.get(5).value) {
                    updateBatteries(5, i, selectedBatteries, batteryBank);
                } else if (i > 5 && i < batteryBank.size() - 5 && battery.index > selectedBatteries.get(5).index && battery.value > selectedBatteries.get(6).value) {
                    updateBatteries(6, i, selectedBatteries, batteryBank);
                } else if (i > 6 && i < batteryBank.size() - 4 && battery.index > selectedBatteries.get(6).index && battery.value > selectedBatteries.get(7).value) {
                    updateBatteries(7, i, selectedBatteries, batteryBank);
                } else if (i > 7 && i < batteryBank.size() - 3 && battery.index > selectedBatteries.get(7).index && battery.value > selectedBatteries.get(8).value) {
                    updateBatteries(8, i, selectedBatteries, batteryBank);
                } else if (i > 8 && i < batteryBank.size() - 2 && battery.index > selectedBatteries.get(8).index && battery.value > selectedBatteries.get(9).value) {
                    updateBatteries(9, i, selectedBatteries, batteryBank);
                } else if (i > 9 && i < batteryBank.size() - 1 && battery.index > selectedBatteries.get(9).index && battery.value > selectedBatteries.get(10).value) {
                    updateBatteries(10, i, selectedBatteries, batteryBank);
                } else if (i > 10 && battery.index > selectedBatteries.get(10).index && battery.value > selectedBatteries.get(11).value) {
                    updateBatteries(11, i, selectedBatteries, batteryBank);
                }
            }

            sum += Long.parseLong("" + selectedBatteries.get(0).value + selectedBatteries.get(1).value + selectedBatteries.get(2).value + selectedBatteries.get(3).value + selectedBatteries.get(4).value + selectedBatteries.get(5).value + selectedBatteries.get(6).value + selectedBatteries.get(7).value + selectedBatteries.get(8).value + selectedBatteries.get(9).value + selectedBatteries.get(10).value + selectedBatteries.get(11).value);
        }

        return String.valueOf(sum);
    }

    private record Battery(int value, int index) {
    }
}