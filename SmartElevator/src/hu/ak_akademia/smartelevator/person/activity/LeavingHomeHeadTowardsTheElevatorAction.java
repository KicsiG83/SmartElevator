package hu.ak_akademia.smartelevator.person.activity;

import java.util.Optional;

import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Destination;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeHeadTowardsTheElevatorAction extends AbstractPersonActivity {

    public LeavingHomeHeadTowardsTheElevatorAction() {
        super("elindult otthonról - lifthez sétál");
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
        int currentDistanceFromTheElevator = Math.min(Math.max(flatDistanceFromTheElevatorInMeters - (int) Math.round(distance), 0), flatDistanceFromTheElevatorInMeters);
        location.setDistanceFromTheElevatorInMeters(currentDistanceFromTheElevator);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        if (person.getLocation()
                .getDistanceFromTheElevatorInMeters() <= 0) {
            Optional<AbsenceTime> currentAbsenceTime = person.getCurrentAbsenceTime(simulationTime);
            if (currentAbsenceTime.isPresent()) {
                Optional<Destination> destination = currentAbsenceTime.get()
                        .getDestination();
                BlockOfFlats blockOfFlats = person.getResidency()
                        .getBlockOfFlats();
                if (destination.isPresent()) {
                    Location current = person.getLocation();
                    Location locationToGoTo = destination.get()
                            .getLocation();
                    if (locationToGoTo.isAbove(current)) {
                        blockOfFlats.signalUpWithElevatorCaller(person);
                        person.setState(PersonState.LEAVING_HOME_WAITING_FOR_THE_ELEVATOR_TO_GO_UP_SOME);
                    } else if (locationToGoTo.isBelow(current)) {
                        blockOfFlats.signalDownWithElevatorCaller(person);
                        person.setState(PersonState.LEAVING_HOME_WAITING_FOR_THE_ELEVATOR_TO_GO_DOWN_SOME);
                    }
                } else {
                    blockOfFlats.signalDownWithElevatorCaller(person);
                    person.setState(PersonState.LEAVING_HOME_WAITING_FOR_THE_ELEVATOR);
                }
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "megérkezett a lifthez.");
            } else {
                person.decrementSatisfaction(50);
                person.setWalkStartTime(new Time(simulationTime));
                person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_FLAT);
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "elkésett, visszafordul.");
            }
        }
    }

}