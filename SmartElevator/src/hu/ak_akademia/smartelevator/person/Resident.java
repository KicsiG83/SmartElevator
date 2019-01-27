package hu.ak_akademia.smartelevator.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Interval;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Scale;
import hu.ak_akademia.smartelevator.util.Time;

public abstract class Resident implements Person {

    private static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    private final Name name;
    private final int ageInYears;
    private final int weightInKilograms;
    private final Occupation occupation;
    private final List<AbsenceTime> absenceTimes;
    private final Map<Time, Action> timeTable;
    private final Scale speedInKilometersPerHour;
    private Flat residency;
    private Location location;
    private final Scale patience;
    private PersonState state = PersonState.AT_HOME;
    private Time walkStartTime;
    private int successfulElevatorRides;
    private int failedElevatorRides;
    private int waitingTimeInSeconds;
    private Scale satisfaction = new Scale(new Interval(0, 100), 50, 1);
    private Elevator travellingWithElevator;

    public Resident(Name name, int ageInYears, int weightInKilograms, Occupation occupation, Scale speedInKilometersPerHour, List<AbsenceTime> absenceTimes, Scale patience) {
        this.name = name;
        this.ageInYears = ageInYears;
        this.weightInKilograms = weightInKilograms;
        this.occupation = occupation;
        this.speedInKilometersPerHour = new Scale(speedInKilometersPerHour);
        List<AbsenceTime> absenceTimesCopy = new ArrayList<>(absenceTimes.size());
        for (AbsenceTime absenceTime : absenceTimes) {
            absenceTimesCopy.add(new AbsenceTime(absenceTime));
        }
        this.absenceTimes = Collections.unmodifiableList(absenceTimesCopy);
        this.patience = new Scale(patience);
        Map<Time, Action> timeTable = new HashMap<>(absenceTimes.size());
        for (AbsenceTime absenceTime : absenceTimesCopy) {
            timeTable.putIfAbsent(absenceTime.getDeparture(), Action.DEPART);
            timeTable.putIfAbsent(absenceTime.getArrival(), Action.ARRIVE);
        }
        this.timeTable = Collections.unmodifiableMap(timeTable);
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public int getAgeInYears() {
        return ageInYears;
    }

    @Override
    public int getWeightInKilograms() {
        return weightInKilograms;
    }

    @Override
    public Occupation getOccupation() {
        return occupation;
    }

    @Override
    public List<AbsenceTime> getAbsenceTimes() {
        return absenceTimes;
    }

    @Override
    public Scale getSpeedInKilometersPerHour() {
        return speedInKilometersPerHour;
    }

    @Override
    public Flat getResidency() {
        return residency;
    }

    @Override
    public void setResidency(Flat residency) {
        this.residency = residency;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Scale getPatience() {
        return patience;
    }

    @Override
    public int incrementPatience() {
        patience.increment();
        return patience.getValue();
    }

    @Override
    public int incrementPatience(int amount) {
        patience.increment(amount);
        return patience.getValue();
    }

    @Override
    public int decrementPatience() {
        patience.decrement();
        return patience.getValue();
    }

    @Override
    public int decrementPatience(int amount) {
        patience.decrement(amount);
        return patience.getValue();
    }

    @Override
    public PersonState getState() {
        return state;
    }

    @Override
    public void setState(PersonState state) {
        this.state = state;
    }

    @Override
    public Time getWalkStartTime() {
        return walkStartTime;
    }

    @Override
    public void setWalkStartTime(Time walkStartTime) {
        this.walkStartTime = walkStartTime;
    }

    @Override
    public int incrementSuccessfulElevatorRides() {
        return ++successfulElevatorRides;
    }

    @Override
    public int getSuccessfulElevatorRides() {
        return successfulElevatorRides;
    }

    @Override
    public int incrementFailedElevatorRides() {
        return ++failedElevatorRides;
    }

    @Override
    public int getFailedElevatorRides() {
        return failedElevatorRides;
    }

    @Override
    public int incrementWaitingTime() {
        return ++waitingTimeInSeconds;
    }

    @Override
    public int getWaitingTimeInSeconds() {
        return waitingTimeInSeconds;
    }

    @Override
    public int incrementSatisfaction() {
        satisfaction.increment();
        return satisfaction.getValue();
    }

    @Override
    public int incrementSatisfaction(int amount) {
        satisfaction.increment(amount);
        return satisfaction.getValue();
    }

    @Override
    public int decrementSatisfaction() {
        satisfaction.decrement();
        return satisfaction.getValue();
    }

    @Override
    public int decrementSatisfaction(int amount) {
        satisfaction.decrement(amount);
        return satisfaction.getValue();
    }

    @Override
    public int getSatisfaction() {
        return satisfaction.getValue();
    }

    @Override
    public boolean isDepartureTime(Time time) {
        return Action.DEPART.equals(timeTable.get(time));
    }

    @Override
    public boolean isArrivalTime(Time time) {
        return Action.ARRIVE.equals(timeTable.get(time));
    }

    @Override
    public Elevator getTravellingWithElevator() {
        return travellingWithElevator;
    }

    @Override
    public void setTravellingWithElevator(Elevator travellingWithElevator) {
        this.travellingWithElevator = travellingWithElevator;
    }

    @Override
    public Optional<AbsenceTime> getCurrentAbsenceTime(Time simulationTime) {
        for (AbsenceTime absenceTime : absenceTimes) {
            if (absenceTime.isWithin(simulationTime)) {
                return Optional.of(absenceTime);
            }
        }
        return Optional.empty();
    }

    @Override
    public void simulate(Time simulationTime) {
        state.getAction()
                .executeAction(this, simulationTime);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(simulationTime + this.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(name);
        builder.append(" (");
        builder.append(ageInYears);
        builder.append("); ");
        builder.append(occupation.getName());
        builder.append(", távollétek ideje: ");
        builder.append(absenceTimes);
        builder.append(", sebesség: ");
        builder.append(speedInKilometersPerHour);
        builder.append(", tartózkodási hely: ");
        builder.append(location);
        builder.append(", várakozási idő: ");
        builder.append(waitingTimeInSeconds);
        builder.append(", türelem: ");
        builder.append(patience);
        builder.append(", elégedettség: ");
        builder.append(satisfaction);
        builder.append(", sikeres lift használat: ");
        builder.append(successfulElevatorRides);
        builder.append(", sikertelen lift használat: ");
        builder.append(failedElevatorRides);
        builder.append(".");
        return builder.toString();
    }

}