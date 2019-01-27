package hu.ak_akademia.smartelevator.person.activity;

import java.util.List;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class ArrivingHomeWaitingForTheElevatorAction extends AbstractPersonActivity {

    public ArrivingHomeWaitingForTheElevatorAction() {
        super("érkezik haza - vár a liftre");
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
        if (tryToTravelWithElevator(person)) {
            person.incrementSatisfaction(30);
            person.setState(PersonState.ARRIVING_HOME_IN_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "hazafelé megy, beszállt a liftbe.");
        } else if (person.getPatience()
                .isAtLowerBound()) {
            person.decrementSatisfaction(50);
            person.incrementFailedElevatorRides();
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_VIA_STAIRS);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "hazafelé megy, megunta a várakozást a liftre, ezért inkább lépcsőn megy.");
        }
    }

    private boolean tryToTravelWithElevator(Person person) {
        Flat residency = person.getResidency();
        BlockOfFlats blockOfFlats = residency.getBlockOfFlats();
        List<Elevator> elevators = blockOfFlats.getElevators();
        for (Elevator elevator : elevators) {
            boolean success = elevator.enter(person)
                    .isOperationSuccessful();
            if (success) {
                person.setTravellingWithElevator(elevator);
                elevator.pressFloorButton(residency.getLocation()
                        .getFloor());
                return true;
            }
        }
        return false;
    }

}