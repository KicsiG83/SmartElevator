package hu.ak_akademia.smartelevator.person.activity;

import java.util.Optional;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeViaStairsGoingOverAction extends AbstractPersonActivity {

    public LeavingHomeViaStairsGoingOverAction() {
        super("elindult otthonról - sétál a lépcsőn másik lakáshoz");
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
        int currentHeightAboveGroundFloor = Math.min(Math.max(flatHeightAboveGroundFloorInMeters - (int) Math.round(distance), 0), flatHeightAboveGroundFloorInMeters);
        location.setHeightAboveGroundFloorInMeters(currentHeightAboveGroundFloor);
        location.setFloor(currentHeightAboveGroundFloor / 3);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        Optional<AbsenceTime> currentAbsenceTime = person.getCurrentAbsenceTime(simulationTime);
        if (currentAbsenceTime.isPresent()) {
            int destinationFloor = currentAbsenceTime.get()
                    .getDestination()
                    .get()
                    .getLocation()
                    .getFloor();
            if (person.getLocation()
                    .getFloor() == destinationFloor) {
                person.setWalkStartTime(new Time(simulationTime));
                person.setState(PersonState.LEAVING_HOME_HEADING_TOWARDS_THE_OTHER_FLAT);
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "átsétált a lépcsőn a másik szintre.");
            }
        } else {
            person.decrementSatisfaction(30);
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_FLAT);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elkésett, visszafordul.");
        }
    }

}