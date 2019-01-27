package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class BeingAwayFromHomeAction extends AbstractPersonActivity {

    public BeingAwayFromHomeAction() {
        super("távol van");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        transitionIfNecessary(person, simulationTime);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        if (person.isArrivalTime(simulationTime)) {
            person.incrementPatience(100);
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "érkezik haza, sétál a lifthez.");
        }
    }

}