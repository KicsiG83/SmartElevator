package hu.ak_akademia.smartelevator.person;

import hu.ak_akademia.smartelevator.util.Time;

public interface PersonActivity {

    String getName();

    void executeAction(Person person, Time simulationTime);

}