package hu.ak_akademia.smartelevator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.elevator.controller.ElevatorController;
import hu.ak_akademia.smartelevator.house.BlockOfFlats;
import hu.ak_akademia.smartelevator.house.BlockOfFlatsOnAvenueQ;
import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonFactory;
import hu.ak_akademia.smartelevator.simulation.SimulationResult;
import hu.ak_akademia.smartelevator.util.RandomNumberGenerator;
import hu.ak_akademia.smartelevator.util.Time;

public class SmartElevatorSimulator {

    private static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    private final List<Long> seeds;
    private final int numberOfResidents;
    private final Class<? extends ElevatorController> elevatorControllerClass;

    public SmartElevatorSimulator(List<Long> seeds, int numberOfResidents, Class<? extends ElevatorController> elevatorControllerClass) {
        this.seeds = seeds;
        this.numberOfResidents = numberOfResidents;
        this.elevatorControllerClass = elevatorControllerClass;
    }

    public List<SimulationResult> simulate() {
        List<SimulationResult> results = new ArrayList<>();
        for (long seed : seeds) {
            SimulationResult simulationResult = new SimulationResult();
            RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(new Random(seed));
            ElevatorController elevatorController;
            try {
                elevatorController = elevatorControllerClass.getDeclaredConstructor()
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException("The provided class cannot be instantiated with its no-argument constructor.", e);
            }
            BlockOfFlats blockOfFlatsOnAvenueQ = new BlockOfFlatsOnAvenueQ(randomNumberGenerator, elevatorController);
            PersonFactory personFactory = new PersonFactory(randomNumberGenerator, blockOfFlatsOnAvenueQ);
            Set<Person> residents = generateResidents(numberOfResidents, simulationResult, personFactory);
            blockOfFlatsOnAvenueQ.populate(residents);
            List<Elevator> elevators = blockOfFlatsOnAvenueQ.getElevators();
            LOGGER.info(blockOfFlatsOnAvenueQ.toString());
            int seconds = 0;
            for (Time simulationTime = new Time(); !simulationTime.isEndOfDay(); simulationTime.incrementSecond()) {
                simulateElevatorController(elevatorController, simulationTime);
                simulateElevators(elevators, simulationTime);
                simulateResidents(residents, simulationTime);
                if (seconds++ % 1000 == 0) {
                    LOGGER.info(String.format("%s: Simulation %3d%% finished.", simulationTime.toString(), (int) (seconds / 86400.0 * 100.0)));
                }
            }
            evaluateResidents(residents, simulationResult);
            evaluateElevators(elevators, simulationResult);
            results.add(simulationResult);
        }
        return results;
    }

    private void evaluateResidents(Set<Person> residents, SimulationResult simulationResult) {
        for (Person resident : residents) {
            simulationResult.registerFedUp(resident.getFailedElevatorRides());
            simulationResult.registerWaitingTime(resident.getWaitingTimeInSeconds());
            simulationResult.registerSatisfaction(resident.getSatisfaction());
        }
    }

    private void evaluateElevators(List<Elevator> elevators, SimulationResult simulationResult) {
        for (Elevator elevator : elevators) {
            simulationResult.registerElevatorStatistics(elevator);
        }
    }

    private void simulateElevatorController(ElevatorController elevatorController, Time simulationTime) {
        elevatorController.simulate(simulationTime);
    }

    private void simulateElevators(List<Elevator> elevators, Time simulationTime) {
        for (Elevator elevator : elevators) {
            elevator.simulate(simulationTime);
        }
    }

    private void simulateResidents(Set<Person> residents, Time simulationTime) {
        for (Person person : residents) {
            person.simulate(simulationTime);
        }
    }

    private Set<Person> generateResidents(int numberOfResidents, SimulationResult simulationResult, PersonFactory personFactory) {
        Set<Person> people = new HashSet<>();
        while (people.size() < numberOfResidents) {
            Person person = personFactory.generatePerson();
            people.add(person);
            simulationResult.registerResident(person);
        }
        return people;
    }

}