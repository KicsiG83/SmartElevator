package hu.ak_akademia.smartelevator.elevator.controller;

import hu.ak_akademia.smartelevator.Simulatable;
import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.util.Time;

public interface ElevatorController extends Simulatable {

    void registerElevator(Elevator elevator);

    void registerCaller(ElevatorCaller caller, int floor);

    void registerDestination(int floor);

    @Override
    void simulate(Time simulationTime);

    ElevatorCaller getCaller(int floor);

}