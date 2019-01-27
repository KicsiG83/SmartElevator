package hu.ak_akademia.smartelevator.person.activity;

import java.util.List;
import java.util.Optional;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeWaitingForTheElevatorGoingOverAction extends AbstractPersonActivity {

    public LeavingHomeWaitingForTheElevatorGoingOverAction() {
        super("elindult otthonról, átmegy másik lakásba - vár a liftre");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        adjustState(person, simulationTime);
        transitionIfNecessary(person, simulationTime);
    }

    private void adjustState(Person person, Time simulationTime) {
        person.decrementPatience();
        person.incrementWaitingTime();
        person.decrementSatisfaction();
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        if (tryToTravelWithElevator(person, simulationTime)) {
            person.incrementSatisfaction(30);
            person.setState(PersonState.LEAVING_HOME_IN_THE_ELEVATOR_GOING_OVER);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "beszállt a liftbe.");
        } else if (person.getPatience()
                .isAtLowerBound()) {
            person.decrementSatisfaction(50);
            person.incrementFailedElevatorRides();
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.LEAVING_HOME_VIA_STAIRS_GOING_OVER);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "megunta a várakozást a liftre, inkább lépcsőn megy.");
        }
    }

    private boolean tryToTravelWithElevator(Person person, Time simulationTime) {
        Optional<AbsenceTime> currentAbsenceTime = person.getCurrentAbsenceTime(simulationTime);
        if (currentAbsenceTime.isPresent()) {
            Flat residency = person.getResidency();
            BlockOfFlats blockOfFlats = residency.getBlockOfFlats();
            List<Elevator> elevators = blockOfFlats.getElevators();
            for (Elevator elevator : elevators) {
                boolean success = elevator.enter(person)
                        .isOperationSuccessful();
                if (success) {
                    int destinationFloor = currentAbsenceTime.get()
                            .getDestination()
                            .get()
                            .getLocation()
                            .getFloor();
                    person.setTravellingWithElevator(elevator);
                    elevator.pressFloorButton(destinationFloor);
                    return true;
                }
            }
        } else {
            person.decrementSatisfaction(50);
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_FLAT);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elkésett, visszafordul.");
        }
        return false;
    }

}