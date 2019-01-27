package hu.ak_akademia.smartelevator.person.activity;

import java.util.Optional;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeInTheElevatorGoingOverAction extends AbstractPersonActivity {

    public LeavingHomeInTheElevatorGoingOverAction() {
        super("elindult otthonról - utazik a lifttel másik lakáshoz");
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
        Optional<AbsenceTime> currentAbsenceTime = person.getCurrentAbsenceTime(simulationTime);
        if (currentAbsenceTime.isPresent()) {
            int destinationFloor = currentAbsenceTime.get()
                    .getDestination()
                    .get()
                    .getLocation()
                    .getFloor();
            if (elevator.getFloor() == destinationFloor) {
                boolean success = elevator.exit(person)
                        .isOperationSuccessful();
                if (success) {
                    person.setTravellingWithElevator(null);
                    person.incrementSuccessfulElevatorRides();
                    person.setWalkStartTime(new Time(simulationTime));
                    person.setState(PersonState.LEAVING_HOME_HEADING_TOWARDS_THE_OTHER_FLAT);
                    LOGGER.info(getLoggerPrefix(person, simulationTime) + "átment a lifttel a másik szintre.");
                }
            }
        } else {
            person.decrementSatisfaction(50);
            Flat residency = person.getResidency();
            elevator.pressFloorButton(residency.getLocation()
                    .getFloor());
            person.setState(PersonState.ARRIVING_HOME_IN_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elkésett, visszafordul.");
        }
    }

}