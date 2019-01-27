package hu.ak_akademia.smartelevator.util;

import java.util.Objects;

public final class Scale {

    private final Interval interval;
    private int value;
    private int rate = 1;

    public Scale(Interval interval) {
        this.interval = interval;
        this.value = interval.getLowerBound();
    }

    public Scale(Interval interval, int value) {
        this.interval = interval;
        setValue(value);
    }

    public Scale(Interval interval, int value, int rate) {
        this.interval = interval;
        setValue(value);
        this.rate = Math.max(rate, 0);
    }

    public Scale(Scale other) {
        this(new Interval(other.interval), other.value, other.rate);
    }

    public int getValue() {
        return value;
    }

    public boolean setValue(int desiredNewValue) {
        int actualNewValue = Math.min(Math.max(desiredNewValue, interval.getLowerBound()), interval.getUpperBound());
        this.value = actualNewValue;
        return desiredNewValue == actualNewValue;
    }

    public void increment() {
        setValue(value + rate);
    }

    public void increment(int delta) {
        setValue(value + delta);
    }

    public void decrement() {
        setValue(value - rate);
    }

    public void decrement(int delta) {
        setValue(value - delta);
    }

    public Interval getInterval() {
        return interval;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = Math.max(rate, 0);
    }

    public boolean isAtUpperBound() {
        return interval.getUpperBound() == value;
    }

    public boolean isAtLowerBound() {
        return interval.getLowerBound() == value;
    }

    public void setToLowerBound() {
        value = interval.getLowerBound();
    }

    public void setToUpperBound() {
        value = interval.getUpperBound();
    }

    @Override
    public int hashCode() {
        return Objects.hash(interval, rate, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Scale)) {
            return false;
        }
        Scale other = (Scale) obj;
        return Objects.equals(interval, other.interval) && rate == other.rate && value == other.value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(value);
        builder.append(" ");
        builder.append(interval);
        return builder.toString();
    }

}