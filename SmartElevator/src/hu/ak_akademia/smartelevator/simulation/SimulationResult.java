package hu.ak_akademia.smartelevator.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.person.Occupation;
import hu.ak_akademia.smartelevator.person.Person;

public class SimulationResult {

    private StatisticElement ageOfResidents = new StatisticElement("Lakosság életkora [év]");
    private Map<Occupation, Integer> residentialOccupationStatistics = new HashMap<>();
    private StatisticElement waitingTimeOfResidents = new StatisticElement("Lakosság várakozási ideje [másodperc]");
    private StatisticElement satisfactionOfResidents = new StatisticElement("Lakosság elégedettség szintje");;
    private int totalNumberOfFedUps;
    private int totalNumberOfTravellersInElevators;
    private Map<String, ElevatorStatistics> elevatorStatistics = new HashMap<>();

    private static class ElevatorStatistics {
        int powerConsumed;
        int totalNumberOfTravellers;

        @Override
        public String toString() {
            return "\n  Lift energiafogyasztása [kWh]: " + String.format("%,d", powerConsumed) + "\n  Lift összes utasainak száma: " + totalNumberOfTravellers;
        }

    }

    public SimulationResult() {
        for (Occupation occupation : Occupation.values()) {
            residentialOccupationStatistics.put(occupation, 0);
        }
    }

    public void registerResident(Person resident) {
        Occupation occupation = resident.getOccupation();
        Integer quantity = residentialOccupationStatistics.get(occupation);
        residentialOccupationStatistics.put(occupation, quantity + 1);
        ageOfResidents.add(resident.getAgeInYears());
    }

    public void registerFedUp(int numberOfFedUps) {
        totalNumberOfFedUps += numberOfFedUps;
    }

    public void registerWaitingTime(int waitingTimeInSeconds) {
        waitingTimeOfResidents.add(waitingTimeInSeconds);
    }

    public void registerSatisfaction(int satisfaction) {
        satisfactionOfResidents.add(satisfaction);
    }

    public void registerElevatorStatistics(Elevator elevator) {
        String elevatorName = elevator.getName();
        ElevatorStatistics elevatorStats;
        if (elevatorStatistics.containsKey(elevatorName)) {
            elevatorStats = elevatorStatistics.get(elevatorName);
        } else {
            elevatorStats = new ElevatorStatistics();
            elevatorStatistics.put(elevatorName, elevatorStats);
        }
        elevatorStats.powerConsumed = elevator.getPowerConsumed();
        elevatorStats.totalNumberOfTravellers = elevator.getTotalNumberOfTravellers();
        totalNumberOfTravellersInElevators += elevator.getTotalNumberOfTravellers();
    }

    public double calculateFinalScore() {
        double normalizedAverageWaitingTimeOfResidents = 100.0 - waitingTimeOfResidents.getNormalizedAverageForRange(0, 300);
        double normalizedAverageSatisfactionOfResidents = satisfactionOfResidents.getNormalizedAverageForRange(0, 100);
        double totalPowerConsumed = 0;
        for (String key : elevatorStatistics.keySet()) {
            totalPowerConsumed += elevatorStatistics.get(key).powerConsumed;
        }
        double normalizedPowerConsumed = 100.0 - totalPowerConsumed / 2_000.0;
        return (normalizedAverageWaitingTimeOfResidents + normalizedAverageSatisfactionOfResidents + normalizedPowerConsumed) / 3.0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Szimulációs eredmény:\n");
        builder.append(ageOfResidents);
        builder.append("\n");
        builder.append("Foglalkozás: ");
        builder.append(residentialOccupationStatistics);
        builder.append("\n");
        builder.append(waitingTimeOfResidents);
        builder.append("\n");
        builder.append(satisfactionOfResidents);
        builder.append("\n");
        builder.append("Lifttel utazók száma: ");
        builder.append(totalNumberOfTravellersInElevators);
        builder.append("\n");
        builder.append("Liftre várakozások feladásának száma: ");
        builder.append(totalNumberOfFedUps);
        builder.append("\n");
        builder.append("Lift statisztikák: \n");
        for (Entry<String, ElevatorStatistics> entry : elevatorStatistics.entrySet()) {
            builder.append(entry.getKey());
            builder.append(" -> ");
            builder.append(entry.getValue());
            builder.append("\n");
        }
        return builder.toString();
    }

}