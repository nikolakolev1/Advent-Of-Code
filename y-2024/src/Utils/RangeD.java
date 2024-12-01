package Utils;

import java.util.Objects;

public class RangeD {
    public final double leftD, rightD;

    public RangeD(double a, double b) {
        this.leftD = Math.min(a, b);
        this.rightD = Math.max(a, b);
    }

    public boolean contains(long a) {
        return leftD <= a && a <= rightD;
    }

    public boolean contains(RangeD a) {
        return leftD <= a.leftD && a.rightD <= rightD;
    }

    public boolean overlaps(RangeD a) {
        return overlapsLower(a) || overlapsUpper(a);
    }

    public boolean overlapsLower(RangeD a) {
        return a.leftD <= leftD && leftD <= a.rightD && a.rightD <= rightD;
    }

    public boolean overlapsUpper(RangeD a) {
        return leftD <= a.leftD && a.leftD <= rightD && rightD <= a.rightD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeD range = (RangeD) o;
        return leftD == range.leftD && rightD == range.rightD;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftD, rightD);
    }

    @Override
    public String toString() {
        return "Range{" +
                "left=" + leftD +
                ", right=" + rightD +
                '}';
    }
}