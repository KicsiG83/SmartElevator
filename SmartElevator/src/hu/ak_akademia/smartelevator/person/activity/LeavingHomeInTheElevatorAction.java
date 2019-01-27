package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeInTheElevatorAction extends AbstractPersonActivity {

    public LeavingHomeInTheElevatorAction() {
        super("elindult otthonról - utazik a lifttel");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        adjustState(person, simulationTime);
        transitionIfNecessary(person, simulationTime);
    }

    private void adjustState(Person person, Time simulationTime) {
        Elevator elevator = person.getTravellingWithElevator();
        Location location = person.getLocation();
        location.setDistanceFromTheElevatorInMeters(0);
        location.setFloor(elevator.getFloor());
        location.setHeightAboveGroundFloorInMeters(elevator.getHeightAboveGroundFloorInMeters());
        person.decrementSatisfaction();
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        Elevator elevator = person.getTravellingWithElevator();
        if (elevator.getFloor() == 0) {
            boolean success = elevator.exit(person)
                    .isOperationSuccessful();
            if (success) {
                person.setTravellingWithElevator(null);
                person.incrementSuccessfulElevatorRides();
                person.setWalkStartTime(new Time(simulationTime));
                person.setState(PersonState.LEAVING_HOME_LEAVING_BUILDING);
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "leért a lifttel a földszintre, sétál ki az épületből.");
            }
        }
    }

}