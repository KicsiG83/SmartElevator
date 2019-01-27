package hu.ak_akademia.smartelevator.util;

import java.util.Objects;

public class Time implements Comparable<Time> {

    private Scale hour;
    private Scale minute;
    private Scale second;

    public Time() {
        this(0, 0, 0);
    }

    public Time(int hour, int minute, int second) {
        this.hour = new Scale(new Interval(0, 23), hour, 1);
        this.minute = new Scale(new Interval(0, 59), minute, 1);
        this.second = new Scale(new Interval(0, 59), second, 1);
    }

    public Time(Time other) {
        this(other.hour.getValue(), other.minute.getValue(), other.second.getValue());
    }

    public Scale getHour() {
        return hour;
    }

    public Scale getMinute() {
        return minute;
    }

    public Scale getSecond() {
        return second;
    }

    public boolean isBeginningOfDay() {
        return hour.isAtLowerBound() && minute.isAtLowerBound() && second.isAtLowerBound();
    }

    public boolean isEndOfDay() {
        return hour.isAtUpperBound() && minute.isAtUpperBound() && second.isAtUpperBound();
    }

    public void incrementHour() {
        if (hour.isAtUpperBound()) {
            hour.setToLowerBound();
        } else {
            hour.increment();
        }
    }

    public void decrementHour() {
        if (hour.isAtLowerBound()) {
            hour.setToUpperBound();
        } else {
            hour.decrement();
        }
    }

    public void incrementMinute() {
        if (minute.isAtUpperBound()) {
            if (hour.isAtUpperBound()) {
                hour.setToLowerBound();
            } else {
                hour.increment();
            }
            minute.setToLowerBound();
        } else {
            minute.increment();
        }
    }

    public void decrementMinute() {
        if (minute.isAtLowerBound()) {
            if (hour.isAtLowerBound()) {
                hour.setToUpperBound();
            } else {
                hour.decrement();
            }
            minute.setToUpperBound();
        } else {
            minute.decrement();
        }
    }

    public void incrementSecond() {
        if (second.isAtUpperBound()) {
            if (minute.isAtUpperBound()) {
                if (hour.isAtUpperBound()) {
                    hour.setToLowerBound();
                } else {
                    hour.increment();
                }
                minute.setToLowerBound();
            } else {
                minute.increment();
            }
            second.setToLowerBound();
        } else {
            second.increment();
        }
    }

    public void decrementSecond() {
        if (second.isAtLowerBound()) {
            if (minute.isAtLowerBound()) {
                if (hour.isAtLowerBound()) {
                    hour.setToUpperBound();
                } else {
                    hour.decrement();
                }
                minute.setToUpperBound();
            } else {
                minute.decrement();
            }
            second.setToUpperBound();
        } else {
            second.decrement();
        }
    }

    public int elapsedSecondsSince(Time startTime) {
        int hourDifference = this.hour.getValue() - startTime.hour.getValue();
        int minuteDifference = this.minute.getValue() - startTime.minute.getValue();
        int secondDifference = this.second.getValue() - startTime.second.getValue();
        int elapsedSeconds = hourDifference * 3600 + minuteDifference * 60 + secondDifference;
        if (elapsedSeconds < 0) {
            throw new IllegalArgumentException("Az eltelt idő kiszámításánál a kezedeti idő nem lehet később, mint a végső idő.");
        }
        return elapsedSeconds;
    }

    public boolean isBefore(Time other) {
        return this.compareTo(other) < 0;
    }

    public boolean isAfter(Time other) {
        return this.compareTo(other) > 0;
    }

    @Override
    public int compareTo(Time other) {
        int hourComparison = Integer.compare(this.hour.getValue(), other.hour.getValue());
        if (hourComparison != 0) {
            return hourComparison;
        }
        int minuteComparison = Integer.compare(this.minute.getValue(), other.minute.getValue());
        if (minuteComparison != 0) {
            return minuteComparison;
        }
        int secondComparison = Integer.compare(this.second.getValue(), other.second.getValue());
        if (secondComparison != 0) {
            return secondComparison;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minute, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Time)) {
            return false;
        }
        Time other = (Time) obj;
        return Objects.equals(hour, other.hour) && Objects.equals(minute, other.minute) && Objects.equals(second, other.second);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour.getValue(), minute.getValue(), second.getValue());
    }

}