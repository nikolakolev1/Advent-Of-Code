package Helpers;

import java.util.Objects;

public class Interval {
    public final int left;
    public final int right;

    public Interval(int x, int y) {
        this.left = Math.min(x, y);
        this.right = Math.max(x, y);
    }

    public boolean contains(int a) {
        return left <= a && a <= right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return left == interval.left && right == interval.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "Interval{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}