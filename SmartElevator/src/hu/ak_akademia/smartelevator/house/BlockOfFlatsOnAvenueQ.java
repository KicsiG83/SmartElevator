package hu.ak_akademia.smartelevator.house;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.elevator.SmartElevator;
import hu.ak_akademia.smartelevator.elevator.controller.ElevatorCaller;
import hu.ak_akademia.smartelevator.elevator.controller.ElevatorController;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.RandomNumberGenerator;

public class BlockOfFlatsOnAvenueQ implements BlockOfFlats {

    private final RandomNumberGenerator randomNumberGenerator;
    private final Map<Integer, Flat> doorNumberFlatMap;
    private final Map<Integer, List<Flat>> floorFlatsMap;
    private final List<Flat> allFlats;
    private final List<Elevator> elevators;
    private final ElevatorController elevatorController;

    public BlockOfFlatsOnAvenueQ(ElevatorController elevatorController) {
        this(new RandomNumberGenerator(), elevatorController);
    }

    public BlockOfFlatsOnAvenueQ(RandomNumberGenerator randomNumberGenerator, ElevatorController elevatorController) {
        Map<Integer, Flat> doorNumberFlatMap = new HashMap<>();
        Map<Integer, List<Flat>> floorFlatsMap = new HashMap<>();
        List<Flat> allFlats = new ArrayList<>();
        for (int floor = 1; floor <= 10; floor++) {
            List<Flat> flatsOnFloor = new ArrayList<>();
            int fromDoorNumber = (floor - 1) * 10 + 1;
            int toDoorNumber = (floor - 1) * 10 + 10;
            for (int doorNumber = fromDoorNumber; doorNumber <= toDoorNumber; doorNumber++) {
                int distanceFromElevatorInMeters = getDistanceFromElevatorInMeters(doorNumber % 10);
                Flat flat = new Flat(this, doorNumber, new Location(floor, distanceFromElevatorInMeters, floor * 3));
                doorNumberFlatMap.put(doorNumber, flat);
                flatsOnFloor.add(flat);
                allFlats.add(flat);
            }
            floorFlatsMap.put(floor, flatsOnFloor);
        }
        for (int floor = 0; floor <= 10; floor++) {
            elevatorController.registerCaller(new ElevatorCaller(floor), floor);
        }
        this.randomNumberGenerator = randomNumberGenerator;
        this.doorNumberFlatMap = Collections.unmodifiableMap(doorNumberFlatMap);
        this.floorFlatsMap = Collections.unmodifiableMap(floorFlatsMap);
        this.allFlats = Collections.unmodifiableList(allFlats);
        this.elevators = List.of(new SmartElevator("kis lift", 320, elevatorController), new SmartElevator("nagy lift", 500, elevatorController));
        this.elevatorController = elevatorController;
        for (Elevator elevator : elevators) {
            elevatorController.registerElevator(elevator);
        }
    }

    private int getDistanceFromElevatorInMeters(int doorNumberOnFloor) {
        switch (doorNumberOnFloor) {
        case 0:
            return 21;
        case 1:
            return 19;
        case 2:
            return 15;
        case 3:
            return 10;
        case 4:
            return 5;
        case 5:
            return 6;
        case 6:
            return 11;
        case 7:
            return 16;
        case 8:
            return 20;
        case 9:
            return 22;
        default:
            throw new IllegalArgumentException("Érvénytelen emeleti ajtószám: " + doorNumberOnFloor);
        }
    }

    @Override
    public void populate(Set<Person> residents) {
        LinkedList<Person> residentsToMoveIn = new LinkedList<>(residents);
        Collections.shuffle(residentsToMoveIn, randomNumberGenerator.getRandom());
        while (!residentsToMoveIn.isEmpty()) {
            Person residentToMoveIn = residentsToMoveIn.remove();
            Flat flatToMoveIn = chooseFlat();
            flatToMoveIn.moveIn(residentToMoveIn);
        }
    }

    private Flat chooseFlat() {
        return allFlats.get(randomNumberGenerator.nextInt(allFlats.size()));
    }

    @Override
    public void signalUpWithElevatorCaller(Person person) {
        ElevatorCaller elevatorCaller = elevatorController.getCaller(person.getLocation()
                .getFloor());
        elevatorCaller.signalUp();
    }

    @Override
    public void signalDownWithElevatorCaller(Person person) {
        ElevatorCaller elevatorCaller = elevatorController.getCaller(person.getLocation()
                .getFloor());
        elevatorCaller.signalDown();
    }

    @Override
    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public List<Flat> getAllFlats() {
        return allFlats;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BlockOfFlatsOnAvenueQ [allFlats=");
        builder.append(allFlats);
        builder.append("]");
        return builder.toString();
    }

}