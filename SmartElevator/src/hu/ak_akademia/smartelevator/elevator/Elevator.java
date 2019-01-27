package hu.ak_akademia.smartelevator.elevator;

import hu.ak_akademia.smartelevator.Simulatable;
import hu.ak_akademia.smartelevator.person.Person;

public interface Elevator extends Simulatable {

    String getName();

    int getDestinationFloor();

    ElevatorResponse setDestinationFloor(int destinationFloor);

    ElevatorResponse start();

    ElevatorResponse stop();

    ElevatorResponse openInnerDoor();

    ElevatorResponse closeInnerDoor();

    /**
     * This method allows the specified person to enter the elevator.
     *
     * @param person
     *            the person who tries to enter the elevator.
     * @return an ElevatorResponse object indicating whether the specified person
     *         could successfully enter the elevator not.
     */
    ElevatorResponse enter(Person person);

    int getTotalNumberOfTravellers();

    /**
     * This method allows the specified person to exit the elevator.
     *
     * @param person
     *            the person who tries to exit the elevator.
     * @return an ElevatorResponse object indicating whether the specified person
     *         could successfully exit the elevator not.
     */
    ElevatorResponse exit(Person person);

    int getHeightAboveGroundFloorInMeters();

    int getFloor();

    boolean isInnerDoorOpen();

    boolean isInnerDoorClosed();

    void pressFloorButton(int floor);

    boolean isAtFloor(int floor);

    int getSecondsSinceDoorOpened();

    int getPowerConsumed();

}