package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class ArrivingHomeHeadingTowardsTheElevatorAction extends AbstractPersonActivity {

    private static final double ENTRANCE_DOOR_DISTANCE_IN_METERS = 10;

    public ArrivingHomeHeadingTowardsTheElevatorAction() {
        super("érkezik haza - sétál a lifthez");
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
            BlockOfFlats blockOfFlats = person.getResidency()
                    .getBlockOfFlats();
            blockOfFlats.signalUpWithElevatorCaller(person);
            person.setState(PersonState.ARRIVING_HOME_WAITING_FOR_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "érkezik haza, odaért a lifthez, vár a liftre.");
        }
    }

}