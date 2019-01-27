package hu.ak_akademia.smartelevator.elevator.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import hu.ak_akademia.smartelevator.elevator.Elevator;

public abstract class AbstractElevatorController implements ElevatorController {

    protected static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    protected final Set<Elevator> elevators = new HashSet<>();
    protected final Map<Integer, ElevatorCaller> callers = new HashMap<>();

    @Override
    public void registerElevator(Elevator elevator) {
        elevators.add(elevator);
        LOGGER.info("Registered elevator: " + elevator);
    }

    @Override
    public void registerCaller(ElevatorCaller caller, int floor) {
        callers.put(floor, caller);
        LOGGER.info("Registered caller:: " + caller + " to floor: " + floor);
    }

    @Override
    public ElevatorCaller getCaller(int floor) {
        return callers.get(floor);
    }

}