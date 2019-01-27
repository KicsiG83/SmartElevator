package hu.ak_akademia.smartelevator.house;

import java.util.List;
import java.util.Set;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.person.Person;

public interface BlockOfFlats {

    void populate(Set<Person> residents);

    void signalDownWithElevatorCaller(Person person);

    void signalUpWithElevatorCaller(Person person);

    List<Elevator> getElevators();

    List<Flat> getAllFlats();

}