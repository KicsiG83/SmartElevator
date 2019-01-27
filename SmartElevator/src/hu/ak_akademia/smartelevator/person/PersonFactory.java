package hu.ak_akademia.smartelevator.person;

import java.util.ArrayList;
import java.util.List;

import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Destination;
import hu.ak_akademia.smartelevator.util.DestinationType;
import hu.ak_akademia.smartelevator.util.Interval;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.RandomNumberGenerator;
import hu.ak_akademia.smartelevator.util.Scale;
import hu.ak_akademia.smartelevator.util.Time;

public class PersonFactory {

    private static final int MIN_AGE = 10;
    private static final int YOUNG_ADULT_THRESHOLD = 18;
    private static final int ADULT_THRESHOLD = 35;
    private static final int OLD_ADULT_THRESHOLD = 60;
    private static final int MAX_AGE = 75;
    private static final int ABSENCE_TIME_DEVIATION_IN_SECONDS = 3_600;
    private static final int STAY_IN_HOUSE_PERCENTAGE = 10;
    private final RandomNumberGenerator randomNumberGenerator;
    private final NameFactory nameFactory;
    private final BlockOfFlats blockOfFlats;

    public PersonFactory(BlockOfFlats blockOfFlats) {
        this.randomNumberGenerator = new RandomNumberGenerator();
        this.nameFactory = new NameFactory(randomNumberGenerator);
        this.blockOfFlats = blockOfFlats;
    }

    public PersonFactory(RandomNumberGenerator randomNumberGenerator, BlockOfFlats blockOfFlats) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.nameFactory = new NameFactory(randomNumberGenerator);
        this.blockOfFlats = blockOfFlats;
    }

    public Person generatePerson() {
        Name name = generateName();
        int ageInYears = generateAgeInYears();
        int weightInKilograms = generateWeightInKilograms(ageInYears);
        Occupation occupation = generateOccupation();
        Scale speedInKilometersPerHour = generateSpeedInKilometersPerHour(occupation);
        List<AbsenceTime> absenceTimes = generateAbsenceTimes(occupation);
        Scale patience = generatePatience();

        if (ageInYears >= MIN_AGE && ageInYears < YOUNG_ADULT_THRESHOLD) {
            return new Child(name, ageInYears, weightInKilograms, occupation, speedInKilometersPerHour, absenceTimes, patience);
        } else if (ageInYears >= YOUNG_ADULT_THRESHOLD && ageInYears < ADULT_THRESHOLD) {
            return new YoungAdult(name, ageInYears, weightInKilograms, occupation, speedInKilometersPerHour, absenceTimes, patience);
        } else if (ageInYears >= ADULT_THRESHOLD && ageInYears < OLD_ADULT_THRESHOLD) {
            return new Adult(name, ageInYears, weightInKilograms, occupation, speedInKilometersPerHour, absenceTimes, patience);
        } else {
            return new OldAdult(name, ageInYears, weightInKilograms, occupation, speedInKilometersPerHour, absenceTimes, patience);
        }
    }

    private int generateAgeInYears() {
        int ageInYears = randomNumberGenerator.nextIntBetween(MIN_AGE, MAX_AGE);
        return ageInYears;
    }

    private int generateWeightInKilograms(int ageInYears) {
        int weightInKilograms = randomNumberGenerator.nextIntBetween(42, 61);
        if (ageInYears < YOUNG_ADULT_THRESHOLD) {
            weightInKilograms -= randomNumberGenerator.nextIntBetween(0, 18);
        } else {
            weightInKilograms += randomNumberGenerator.nextIntBetween(0, 18);
        }
        return weightInKilograms;
    }

    private Scale generateSpeedInKilometersPerHour(Occupation occupation) {
        Interval speedLimits = occupation.getSpeedLimits();
        int speed = randomNumberGenerator.nextIntBetween(speedLimits);
        Scale speedInKilometersPerHour = new Scale(speedLimits, speed, 1);
        return speedInKilometersPerHour;
    }

    private List<AbsenceTime> generateAbsenceTimes(Occupation occupation) {
        List<AbsenceTime> absenceTimes = new ArrayList<>();
        List<AbsenceTime> typicalAbsenceTimes = occupation.getTypicalAbsenceTimes();
        for (AbsenceTime typicalAbsenceTime : typicalAbsenceTimes) {
            Time departure = generateAbsenceTime(typicalAbsenceTime.getDeparture(), randomNumberGenerator.nextBoolean(), randomNumberGenerator.nextInt(ABSENCE_TIME_DEVIATION_IN_SECONDS));
            Time arrival = generateAbsenceTime(typicalAbsenceTime.getArrival(), randomNumberGenerator.nextBoolean(), randomNumberGenerator.nextInt(ABSENCE_TIME_DEVIATION_IN_SECONDS));
            absenceTimes.add(new AbsenceTime(generateDestination(), departure, arrival));
        }
        return absenceTimes;
    }

    private Destination generateDestination() {
        if (randomNumberGenerator.nextInt(100) < STAY_IN_HOUSE_PERCENTAGE) {
            return new Destination(DestinationType.STAY_IN_HOUSE, generateLocation());
        }
        return null;
    }

    private Location generateLocation() {
        List<Flat> allFlats = blockOfFlats.getAllFlats();
        int randomFlatIndex = randomNumberGenerator.nextInt(allFlats.size());
        return allFlats.get(randomFlatIndex)
                .getLocation();
    }

    private Occupation generateOccupation() {
        Occupation[] occupations = Occupation.values();
        Occupation occupation = occupations[randomNumberGenerator.nextInt(occupations.length)];
        return occupation;
    }

    private Scale generatePatience() {
        return new Scale(new Interval(0, 250), randomNumberGenerator.nextIntBetween(0, 250), randomNumberGenerator.nextIntBetween(0, 4));
    }

    private Name generateName() {
        return nameFactory.generateName();
    }

    private Time generateAbsenceTime(Time baseTime, boolean positiveDeviation, int absenceTimeDeviationInSeconds) {
        Time time = new Time(baseTime);
        for (int i = 0; i < absenceTimeDeviationInSeconds; i++) {
            if (positiveDeviation) {
                time.incrementSecond();
            } else {
                time.decrementSecond();
            }
        }
        return time;
    }

}