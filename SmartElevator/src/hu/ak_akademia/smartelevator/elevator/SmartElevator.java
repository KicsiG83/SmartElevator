package hu.ak_akademia.smartelevator.elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import hu.ak_akademia.smartelevator.elevator.controller.ElevatorController;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.util.Interval;
import hu.ak_akademia.smartelevator.util.Scale;
import hu.ak_akademia.smartelevator.util.Time;

public class SmartElevator implements Elevator {

    private static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    private final String name;
    private ElevatorState state = ElevatorState.IDLE;
    private final int maximumWeightCarryingCapacity;
    private final Scale floor = new Scale(new Interval(0, 10), 0, 1);
    private DoorState doorState = DoorState.CLOSED;
    private final Scale doorOpenness = new Scale(new Interval(0, 100), 0, 20);
    private final Scale heightAboveGroundFloorInMeters = new Scale(new Interval(0, 30), 0, 1);
    private final Scale rotorSpeed = new Scale(new Interval(0, 5), 0, 1);
    private final List<Person> travellers = new ArrayList<>();
    private int numberOfTravellers;
    private final ElevatorController controller;
    private final Scale destinationFloor = new Scale(new Interval(0, 10), 0, 1);
    private final Scale doorOpenedForSeconds = new Scale(new Interval(0, 86_400));
    private int powerConsumed;

    public SmartElevator(String name, int maximumWeightCarryingCapacity, ElevatorController elevatorController) {
        this.name = name;
        this.maximumWeightCarryingCapacity = maximumWeightCarryingCapacity;
        this.controller = elevatorController;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDestinationFloor() {
        return destinationFloor.getValue();
    }

    @Override
    public ElevatorResponse setDestinationFloor(int destinationFloor) {
        boolean newValueSuccessfullySet = this.destinationFloor.setValue(destinationFloor);
        if (newValueSuccessfullySet) {
            return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "Új célállomás beállítva.", true);
        } else {
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "Új célállomás beállítva (túlvezérlés).", true);
        }
    }

    @Override
    public ElevatorResponse start() {
        if (state.equals(ElevatorState.MOVING)) {
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift már mozog.", false);
        }
        if (!doorState.equals(DoorState.CLOSED)) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A lift nem indítható, mert nyitva van az ajtaja.", false);
        }
        state = ElevatorState.MOVING;
        return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A lift elindult.", true);
    }

    @Override
    public ElevatorResponse stop() {
        if (state.equals(ElevatorState.IDLE)) {
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift már áll.", false);
        }
        state = ElevatorState.IDLE;
        return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A lift megállt.", true);
    }

    @Override
    public ElevatorResponse openInnerDoor() {
        if (!isAllignedToLevel()) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A lift ajtaja nem nyitható, mert nincs szintben az emelettel.", false);
        }
        switch (doorState) {
        case OPENING:
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift ajtaja már nyílik.", false);
        case OPENED:
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift ajtaja már nyitva van.", false);
        case STUCK:
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A lift ajtaja nem nyitható, mert beragadt.", false);
        default:
            doorState = DoorState.OPENING;
            return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A lift ajtaja nyílik.", true);
        }
    }

    @Override
    public ElevatorResponse closeInnerDoor() {
        if (!isAllignedToLevel()) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A lift ajtaja nem zárható, mert nincs szintben az emelettel.", false);
        }
        switch (doorState) {
        case CLOSING:
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift ajtaja már záródik.", false);
        case CLOSED:
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A lift ajtaja már zárva van.", false);
        case STUCK:
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A lift ajtaja nem zárható, mert beragadt.", false);
        default:
            doorState = DoorState.CLOSING;
            return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A lift ajtaja záródik.", true);
        }
    }

    @Override
    public ElevatorResponse enter(Person person) {
        if (floor.getValue() != person.getLocation()
                .getFloor()) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A megadott személy nem szállhat be a liftbe, mert nincs azonos szintben a lifttel.", false);
        }
        if (!isInnerDoorOpen()) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A megadott személy nem szállhat be a liftbe, mert nincs nyitva a lift ajtaja.", false);
        }
        int sumWeightInKilograms = getSumWeightOfTravellersInKilograms();
        if (sumWeightInKilograms + person.getWeightInKilograms() > maximumWeightCarryingCapacity) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A megadott személy nem szállhat be, mert a lift túlterhelt lenne.", false);
        }
        travellers.add(person);
        numberOfTravellers++;
        return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A megadott személy sikeresen beszállt a liftbe.", true);
    }

    private int getSumWeightOfTravellersInKilograms() {
        int sum = 0;
        for (Person traveller : travellers) {
            sum += traveller.getWeightInKilograms();
        }
        return sum;
    }

    @Override
    public int getTotalNumberOfTravellers() {
        return numberOfTravellers;
    }

    @Override
    public ElevatorResponse exit(Person person) {
        if (!isInnerDoorOpen()) {
            return new ElevatorResponse(ElevatorResponseCode.ERROR, "A megadott személy nem szállhat ki a liftből, mert nincs nyitva a lift ajtaja.", false);
        }
        if (travellers.remove(person)) {
            return new ElevatorResponse(ElevatorResponseCode.SUCCESS, "A megadott személy sikeresen kiszállt a liftből.", true);
        } else {
            return new ElevatorResponse(ElevatorResponseCode.WARNING, "A megadott személy nem szállhat ki a liftből, mert nincs a liftben.", false);
        }
    }

    @Override
    public int getHeightAboveGroundFloorInMeters() {
        return heightAboveGroundFloorInMeters.getValue();
    }

    @Override
    public int getFloor() {
        return floor.getValue();
    }

    @Override
    public boolean isInnerDoorOpen() {
        return DoorState.OPENED.equals(doorState);
    }

    @Override
    public boolean isInnerDoorClosed() {
        return DoorState.CLOSED.equals(doorState);
    }

    @Override
    public void pressFloorButton(int floor) {
        controller.registerDestination(floor);
    }

    private void adjustFloorToHeightAboveGroundFloorInMeters() {
        floor.setValue(heightAboveGroundFloorInMeters.getValue() / 3);
    }

    private boolean isAllignedToLevel() {
        return floor.getValue() * 3 == heightAboveGroundFloorInMeters.getValue();
    }

    @Override
    public boolean isAtFloor(int floor) {
        return this.floor.getValue() == floor && isAllignedToLevel();
    }

    @Override
    public int getSecondsSinceDoorOpened() {
        return doorOpenedForSeconds.getValue();
    }

    @Override
    public int getPowerConsumed() {
        return powerConsumed;
    }

    @Override
    public void simulate(Time simulationTime) {
        if (state.equals(ElevatorState.MOVING)) {
            powerConsumed += 5;
        }
        if (doorState.equals(DoorState.OPENING) || doorState.equals(DoorState.CLOSING)) {
            powerConsumed += 1;
        }
        if (doorState.equals(DoorState.OPENED)) {
            doorOpenedForSeconds.increment();
        } else {
            doorOpenedForSeconds.setToLowerBound();
        }
        if (state.equals(ElevatorState.IDLE)) {
            if (doorState.equals(DoorState.OPENING)) {
                doorOpenness.increment();
                if (doorOpenness.isAtUpperBound()) {
                    doorState = DoorState.OPENED;
                }
            } else if (doorState.equals(DoorState.CLOSING)) {
                doorOpenness.decrement();
                if (doorOpenness.isAtLowerBound()) {
                    doorState = DoorState.CLOSED;
                }
            }
        } else if (state.equals(ElevatorState.MOVING)) {
            if (destinationFloor.getValue() * 3 > heightAboveGroundFloorInMeters.getValue()) {
                if (rotorSpeed.isAtUpperBound()) {
                    heightAboveGroundFloorInMeters.increment();
                } else {
                    rotorSpeed.increment();
                }
            } else if (destinationFloor.getValue() * 3 < heightAboveGroundFloorInMeters.getValue()) {
                if (rotorSpeed.isAtLowerBound()) {
                    heightAboveGroundFloorInMeters.decrement();
                } else {
                    rotorSpeed.decrement();
                }
            } else {
                state = ElevatorState.IDLE;
            }
            adjustFloorToHeightAboveGroundFloorInMeters();
        }
        LOGGER.fine(simulationTime + this.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SmartElevator [name=");
        builder.append(name);
        builder.append(", state=");
        builder.append(state);
        builder.append(", maximumWeightCarryingCapacity=");
        builder.append(maximumWeightCarryingCapacity);
        builder.append(", floor=");
        builder.append(floor);
        builder.append(", doorState=");
        builder.append(doorState);
        builder.append(", doorOpenness=");
        builder.append(doorOpenness);
        builder.append(", heightAboveGroundFloorInMeters=");
        builder.append(heightAboveGroundFloorInMeters);
        builder.append(", travellers=");
        builder.append(travellers);
        builder.append(", numberOfTravellers=");
        builder.append(numberOfTravellers);
        builder.append(", controller=");
        builder.append(controller);
        builder.append(", destinationFloor=");
        builder.append(destinationFloor);
        builder.append("]");
        return builder.toString();
    }

}