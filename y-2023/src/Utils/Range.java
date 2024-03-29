package Utils;

import java.util.Objects;

public class Range {
    public long left, right;
    public double leftD, rightD;

    public Range(long a, long b) {
        this.left = Math.min(a, b);
        this.right = Math.max(a, b);
    }

    public Range(double a, double b) {
        this.leftD = Math.min(a, b);
        this.rightD = Math.max(a, b);
    }

    public boolean contains(long a) {
        return left <= a && a <= right;
    }

    public boolean contains(Range a) {
        return left <= a.left && a.right <= right;
    }

    public boolean overlaps(Range a) {
        return overlapsLower(a) || overlapsUpper(a);
    }

    public boolean overlapsLower(Range a) {
        return a.left <= left && left <= a.right && a.right <= right;
    }

    public boolean overlapsUpper(Range a) {
        return left <= a.left && a.left <= right && right <= a.right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return left == range.left && right == range.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "Range{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}