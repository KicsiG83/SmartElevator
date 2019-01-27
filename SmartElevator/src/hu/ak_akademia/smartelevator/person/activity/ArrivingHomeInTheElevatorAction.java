package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class ArrivingHomeInTheElevatorAction extends AbstractPersonActivity {

    public ArrivingHomeInTheElevatorAction() {
        super("érkezik haza - utazik a lifttel");
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
        int destinationFloor = person.getResidency()
                .getLocation()
                .getFloor();
        Elevator elevator = person.getTravellingWithElevator();
        if (elevator.getFloor() == destinationFloor) {
            boolean success = elevator.exit(person)
                    .isOperationSuccessful();
            if (success) {
                person.setTravellingWithElevator(null);
                person.incrementSuccessfulElevatorRides();
                person.setWalkStartTime(new Time(simulationTime));
                person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_FLAT);
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "hazafelé megy, kiszállt a liftből, sétál haza.");
            }
        }
    }

}