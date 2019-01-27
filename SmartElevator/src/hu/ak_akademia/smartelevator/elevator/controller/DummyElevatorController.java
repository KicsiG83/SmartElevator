package hu.ak_akademia.smartelevator.elevator.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.util.Time;

public class DummyElevatorController extends AbstractElevatorController {

    private final Set<Integer> requestedDestinations = new HashSet<>();
    private final Set<Integer> elevator2Destinations = new LinkedHashSet<>();

    @Override
    public void registerDestination(int floor) {
        requestedDestinations.add(floor);
    }

    @Override
    public void simulate(Time simulationTime) {
        Iterator<Elevator> elevatorIterator = elevators.iterator();
        Elevator elevator1 = elevatorIterator.next();
        Elevator elevator2 = elevatorIterator.next();
        processCallerSignals();
        updateElevator2Destinations();
        control(elevator1, requestedDestinations);
        control(elevator2, elevator2Destinations);
        LOGGER.fine(simulationTime + this.toString());
    }

    private void processCallerSignals() {
        for (Entry<Integer, ElevatorCaller> entry : callers.entrySet()) {
            Integer floor = entry.getKey();
            ElevatorCaller elevatorCaller = entry.getValue();
            if (elevatorCaller.isSignalledInEitherWay()) {
                requestedDestinations.add(floor);
                elevatorCaller.clear();
            }
        }
    }

    private void updateElevator2Destinations() {
        if (elevator2Destinations.isEmpty()) {
            for (int floor = 10; floor >= 0; floor--) {
                elevator2Destinations.add(floor);
            }
        }
    }

    private void control(Elevator elevator, Set<Integer> requestedDestinations) {
        if (!requestedDestinations.isEmpty()) {
            Iterator<Integer> requestedFloorIterator = requestedDestinations.iterator();
            int nextFloorToGoTo = requestedFloorIterator.next();
            if (elevator.isAtFloor(nextFloorToGoTo)) {
                if (elevator.isInnerDoorOpen()) {
                    if (elevator.getSecondsSinceDoorOpened() >= 10) {
                        elevator.closeInnerDoor();
                        requestedFloorIterator.remove();
                    }
                } else {
                    elevator.openInnerDoor();
                }
            } else {
                if (elevator.getDestinationFloor() != nextFloorToGoTo) {
                    if (elevator.isInnerDoorClosed()) {
                        elevator.setDestinationFloor(nextFloorToGoTo);
                    } else {
                        elevator.closeInnerDoor();
                    }
                }
                elevator.start();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DummyElevatorController [requestedDestinations=");
        builder.append(requestedDestinations);
        builder.append(", elevator2Destinations=");
        builder.append(elevator2Destinations);
        builder.append("]");
        return builder.toString();
    }

}