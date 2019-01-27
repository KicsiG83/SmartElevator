package hu.ak_akademia.smartelevator.util;

import java.util.Objects;

public class Interval {

    private final int lowerBound;
    private final int upperBound;

    public Interval(int lowerBound, int upperBound) {
        this.lowerBound = Math.min(lowerBound, upperBound);
        this.upperBound = Math.max(lowerBound, upperBound);
    }

    public Interval(Interval other) {
        this(other.getLowerBound(), other.getUpperBound());
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Interval)) {
            return false;
        }
        Interval other = (Interval) obj;
        return lowerBound == other.lowerBound && upperBound == other.upperBound;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(lowerBound);
        builder.append(", ");
        builder.append(upperBound);
        builder.append("]");
        return builder.toString();
    }

}