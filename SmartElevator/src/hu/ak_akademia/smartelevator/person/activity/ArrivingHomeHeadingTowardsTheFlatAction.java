package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class ArrivingHomeHeadingTowardsTheFlatAction extends AbstractPersonActivity {

    public ArrivingHomeHeadingTowardsTheFlatAction() {
        super("érkezik haza - lakáshoz sétál");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        adjustDistanceFromTheElevator(person, simulationTime);
        transitionIfNecessary(person, simulationTime);
    }

    private void adjustDistanceFromTheElevator(Person person, Time simulationTime) {
        Location location = person.getLocation();
        double conversionRate = 3.6;
        int speedInKilometersPerHour = person.getSpeedInKilometersPerHour()
                .getValue();
        double speedInMetersPerSecond = speedInKilometersPerHour / conversionRate;
        Time walkStartTime = person.getWalkStartTime();
        int elapsedSeconds = simulationTime.elapsedSecondsSince(walkStartTime);
        double distance = speedInMetersPerSecond * elapsedSeconds;
        int flatDistanceFromTheElevatorInMeters = person.getResidency()
                .getLocation()
                .getDistanceFromTheElevatorInMeters();
        int currentDistanceFromTheElevator = Math.min(Math.max((int) Math.round(distance), 0), flatDistanceFromTheElevatorInMeters);
        location.setDistanceFromTheElevatorInMeters(currentDistanceFromTheElevator);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        int flatDistanceFromTheElevatorInMeters = person.getResidency()
                .getLocation()
                .getDistanceFromTheElevatorInMeters();
        if (person.getLocation()
                .getDistanceFromTheElevatorInMeters() <= flatDistanceFromTheElevatorInMeters) {
            person.setState(PersonState.AT_HOME);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "hazafelé megy, hazaérkezett.");
        }
    }

}