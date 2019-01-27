package hu.ak_akademia.smartelevator.simulation;

public class StatisticElement {

    private final String name;
    private int quantity;
    private double minimum;
    private double maximum;
    private double sum;
    private double average;
    private boolean dataAvailable;

    public StatisticElement(String name) {
        this.name = name;
    }

    public void add(double value) {
        quantity++;
        minimum = dataAvailable ? Math.min(minimum, value) : value;
        maximum = dataAvailable ? Math.max(maximum, value) : value;
        sum += value;
        average = sum / quantity;
        dataAvailable = true;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getSum() {
        return sum;
    }

    public double getAverage() {
        return average;
    }

    public boolean isDataAvailable() {
        return dataAvailable;
    }

    public double getNormalizedAverageForRange(int lowerBound, int upperBound) {
        double movedAverage = average - lowerBound;
        double range = upperBound - lowerBound;
        return movedAverage / range * 100.0;
    }

    @Override
    public String toString() {
        if (dataAvailable) {
            StringBuilder builder = new StringBuilder();
            builder.append(name);
            builder.append(": [mennyiség=");
            builder.append(String.format("%,d", quantity));
            builder.append(", összeg=");
            builder.append(String.format("%,.4f", sum));
            builder.append(", minimum=");
            builder.append(String.format("%,.4f", minimum));
            builder.append(", maximum=");
            builder.append(String.format("%,.4f", maximum));
            builder.append(", average=");
            builder.append(String.format("%,.4f", average));
            builder.append("]");
            return builder.toString();
        } else {
            return name + ": [Nincs adat]";
        }
    }

}