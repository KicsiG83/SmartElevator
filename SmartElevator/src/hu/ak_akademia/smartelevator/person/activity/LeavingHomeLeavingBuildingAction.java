package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeLeavingBuildingAction extends AbstractPersonActivity {

    private static final double ENTRANCE_DOOR_DISTANCE_IN_METERS = 10;

    public LeavingHomeLeavingBuildingAction() {
        super("elindult otthonról - sétál ki az épületből");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        transitionIfNecessary(person, simulationTime);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        double conversionRate = 3.6;
        int speedInKilometersPerHour = person.getSpeedInKilometersPerHour()
                .getValue();
        double speedInMetersPerSecond = speedInKilometersPerHour / conversionRate;
        Time walkStartTime = person.getWalkStartTime();
        int elapsedSeconds = simulationTime.elapsedSecondsSince(walkStartTime);
        double distance = speedInMetersPerSecond * elapsedSeconds;
        if (distance >= ENTRANCE_DOOR_DISTANCE_IN_METERS) {
            person.setState(PersonState.AWAY);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elhagyta az épületet.");
        }
    }

}