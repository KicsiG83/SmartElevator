package hu.ak_akademia.smartelevator.person.activity;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.util.Time;

public class IdleAction extends AbstractPersonActivity {

    public IdleAction() {
        super("tétlen");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        LOGGER.fine(simulationTime + " - " + person + ": Tétlen.");
    }

}