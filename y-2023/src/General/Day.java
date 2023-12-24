package General;

public interface Day {
    void loadData(String filename);

    String part1();

    String part2();

    default void reset() {
    }
}