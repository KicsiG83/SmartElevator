package hu.ak_akademia.smartelevator.person.activity;

import java.util.logging.Logger;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonActivity;
import hu.ak_akademia.smartelevator.util.Time;

public abstract class AbstractPersonActivity implements PersonActivity {

    protected static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    protected final String name;

    public AbstractPersonActivity(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    protected String getLoggerPrefix(Person person, Time simulationTime) {
        return simulationTime + " - " + person + ": ";
    }

}