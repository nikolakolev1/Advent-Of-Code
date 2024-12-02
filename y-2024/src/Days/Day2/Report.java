package Days.Day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Report {
    private int[] levels;
    private boolean increasing;

    public Report(String levels) {
        this.levels = Arrays.stream(levels.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public boolean isValid_Part1() {
        // Set if the levels are increasing or decreasing
        increasing = this.levels[0] < this.levels[1];

        for (int i = 1; i < levels.length; i++) {
            /*
             * Check:
             * 1) if the levels are following the increasing or decreasing trend
             * 2) if the difference between the levels is less than or equal to 3
             */
            if (!followsTrend(i) || !smallDifference(i)) return false;
        }

        return true;
    }

    public boolean isValid_Part2() {
        if (isValid_Part1()) return true;

        List<int[]> levelsVariations = new ArrayList<>();
        for (int i = 0; i < levels.length; i++) {
            int[] levelsVariation = new int[levels.length - 1];

            System.arraycopy(levels, 0, levelsVariation, 0, i);
            System.arraycopy(levels, i + 1, levelsVariation, i, levels.length - i - 1);

            levelsVariations.add(levelsVariation);
        }

        for (int[] levels : levelsVariations) {
            this.levels = levels;
            if (isValid_Part1()) return true;
        }

        return false;
    }

    private boolean followsTrend(int i) {
        return increasing ? levels[i - 1] < levels[i] : levels[i - 1] > levels[i];
    }

    private boolean smallDifference(int i) {
        return Math.abs(levels[i - 1] - levels[i]) <= 3;
    }
}