package General;

import java.util.Objects;

public class IntervalLong {
    public final long left;
    public final long right;

    public IntervalLong(long x, long y) {
        this.left = Math.min(x, y);
        this.right = Math.max(x, y);
    }

    public boolean contains(long a) {
        return left <= a && a <= right;
    }

    public boolean contains(IntervalLong a) {
        return left <= a.left && a.right <= right;
    }

    public boolean overlaps(IntervalLong a) {
        return overlapsLower(a) || overlapsUpper(a);
    }

    public boolean overlapsLower(IntervalLong a) {
        return a.left <= left && left <= a.right && a.right <= right;
    }

    public boolean overlapsUpper(IntervalLong a) {
        return left <= a.left && a.left <= right && right <= a.right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntervalLong interval = (IntervalLong) o;
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