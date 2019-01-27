package hu.ak_akademia.smartelevator.util;

import java.util.Random;

public class RandomNumberGenerator {

    private final Random random;

    public RandomNumberGenerator() {
        this.random = new Random();
    }

    public RandomNumberGenerator(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }

    public int nextIntBetween(int lowerBound, int upperBound) {
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    public int nextIntBetween(Interval interval) {
        return nextIntBetween(interval.getLowerBound(), interval.getUpperBound());
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

}