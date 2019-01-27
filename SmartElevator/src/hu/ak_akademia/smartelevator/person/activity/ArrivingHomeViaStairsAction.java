package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class ArrivingHomeViaStairsAction extends AbstractPersonActivity {

    public ArrivingHomeViaStairsAction() {
        super("érkezik haza - sétál a lépcsőn");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        adjustLocation(person, simulationTime);
        transitionIfNecessary(person, simulationTime);
    }

    private void adjustLocation(Person person, Time simulationTime) {
        Location location = person.getLocation();
        double conversionRate = 3.6;
        int speedInKilometersPerHour = person.getSpeedInKilometersPerHour()
                .getValue();
        double speedInMetersPerSecond = speedInKilometersPerHour / conversionRate;
        Time walkStartTime = person.getWalkStartTime();
        int elapsedSeconds = simulationTime.elapsedSecondsSince(walkStartTime);
        double distance = speedInMetersPerSecond * elapsedSeconds;
        int flatHeightAboveGroundFloorInMeters = person.getResidency()
                .getLocation()
                .getHeightAboveGroundFloorInMeters();
        int currentHeightAboveGroundFloor = Math.min(Math.max((int) Math.round(distance), 0), flatHeightAboveGroundFloorInMeters);
        location.setHeightAboveGroundFloorInMeters(currentHeightAboveGroundFloor);
        location.setFloor(currentHeightAboveGroundFloor / 3);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        int flatHeightAboveGroundFloorInMeters = person.getResidency()
                .getLocation()
                .getHeightAboveGroundFloorInMeters();
        if (person.getLocation()
                .getHeightAboveGroundFloorInMeters() >= flatHeightAboveGroundFloorInMeters) {
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_FLAT);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "hazafelé megy, felért a lépcsőn.");
        }
    }

}