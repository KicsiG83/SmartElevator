package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.Time;

public class BeingHomeAction extends AbstractPersonActivity {

    public BeingHomeAction() {
        super("otthon tartózkodik");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        transitionIfNecessary(person, simulationTime);
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        if (person.isDepartureTime(simulationTime)) {
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.LEAVING_HOME_HEADING_TOWARDS_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elindul otthonról.");
        }
    }

}