package hu.ak_akademia.smartelevator;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import hu.ak_akademia.smartelevator.elevator.controller.DummyElevatorController;
import hu.ak_akademia.smartelevator.simulation.SimulationResult;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("ElevatorLogger");

    public static void main(String[] args) {
        setupLogger();
        LOGGER.info("Elevator simulation started.");
        List<Long> seeds = List.of(20181224L, 20181225L, 20181226L, 20190118L, 20190119L, 20190120L, 20191224L, 20191225L, 20191226L, 20200101L);
        SmartElevatorSimulator smartElevatorSimulator = new SmartElevatorSimulator(seeds, 300, DummyElevatorController.class);
        List<SimulationResult> simulationResults = smartElevatorSimulator.simulate();
        for (SimulationResult simulationResult : simulationResults) {
            LOGGER.info(simulationResult.toString());
            LOGGER.info("Score: " + simulationResult.calculateFinalScore());
        }
        double finalScore = calculateFinalScore(simulationResults);
        LOGGER.info("Final score: " + finalScore);
        LOGGER.info("Elevator simulation finished.");
    }

    private static void setupLogger() {
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
            FileHandler fileHandler = new FileHandler("log/elevator-simulation.log", false);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            // LOGGER.setLevel(Level.FINE); // if necessary for debug
            LOGGER.setLevel(Level.INFO);
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculateFinalScore(List<SimulationResult> simulationResults) {
        double finalScore = 0.0;
        for (SimulationResult simulationResult : simulationResults) {
            finalScore += simulationResult.calculateFinalScore();
        }
        return finalScore / simulationResults.size();
    }

}