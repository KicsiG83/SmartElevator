package hu.ak_akademia.smartelevator.person.activity;

import java.util.List;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.elevator.ElevatorResponse;
import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeWaitingForTheElevatorAction extends AbstractPersonActivity {

    public LeavingHomeWaitingForTheElevatorAction() {
        super("elindult otthonról - vár a liftre");
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
            person.setState(PersonState.LEAVING_HOME_IN_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "beszállt a liftbe.");
        } else if (person.getPatience()
                .isAtLowerBound()) {
            person.decrementSatisfaction(50);
            person.incrementFailedElevatorRides();
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.LEAVING_HOME_VIA_STAIRS);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "megunta a várakozást a liftre, inkább lépcsőn megy.");
        }
    }

    private boolean tryToTravelWithElevator(Person person, Time simulationTime) {
        Flat residency = person.getResidency();
        BlockOfFlats blockOfFlats = residency.getBlockOfFlats();
        List<Elevator> elevators = blockOfFlats.getElevators();
        for (Elevator elevator : elevators) {
            ElevatorResponse enterResult = elevator.enter(person);
            boolean success = enterResult.isOperationSuccessful();
            if (success) {
                person.setTravellingWithElevator(elevator);
                elevator.pressFloorButton(0);
                return true;
            } else {
                LOGGER.fine(getLoggerPrefix(person, simulationTime) + enterResult.getResponseMessage());
            }
        }
        return false;
    }

}