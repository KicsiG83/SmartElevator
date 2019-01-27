package hu.ak_akademia.smartelevator.person.activity;

import java.util.Optional;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.person.PersonState;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Time;

public class LeavingHomeHeadingTowardsTheOtherFlatAction extends AbstractPersonActivity {

    public LeavingHomeHeadingTowardsTheOtherFlatAction() {
        super("elindult otthonról - másik lakáshoz sétál");
    }

    @Override
    public void executeAction(Person person, Time simulationTime) {
        adjustDistanceFromTheElevator(person, simulationTime);
        transitionIfNecessary(person, simulationTime);
    }

    private void adjustDistanceFromTheElevator(Person person, Time simulationTime) {
        Optional<AbsenceTime> absenceTime = person.getCurrentAbsenceTime(simulationTime);
        if (absenceTime.isPresent()) {
            Location location = person.getLocation();
            double conversionRate = 3.6;
            int speedInKilometersPerHour = person.getSpeedInKilometersPerHour()
                    .getValue();
            double speedInMetersPerSecond = speedInKilometersPerHour / conversionRate;
            Time walkStartTime = person.getWalkStartTime();
            int elapsedSeconds = simulationTime.elapsedSecondsSince(walkStartTime);
            double distance = speedInMetersPerSecond * elapsedSeconds;
            int flatDistanceFromTheElevatorInMeters = absenceTime.get()
                    .getDestination()
                    .get()
                    .getLocation()
                    .getDistanceFromTheElevatorInMeters();
            int currentDistanceFromTheElevator = Math.min(Math.max((int) Math.round(distance), 0), flatDistanceFromTheElevatorInMeters);
            location.setDistanceFromTheElevatorInMeters(currentDistanceFromTheElevator);
        }
    }

    private void transitionIfNecessary(Person person, Time simulationTime) {
        Optional<AbsenceTime> absenceTime = person.getCurrentAbsenceTime(simulationTime);
        if (absenceTime.isPresent()) {
            int flatDistanceFromTheElevatorInMeters = absenceTime.get()
                    .getDestination()
                    .get()
                    .getLocation()
                    .getDistanceFromTheElevatorInMeters();
            if (person.getLocation()
                    .getDistanceFromTheElevatorInMeters() <= flatDistanceFromTheElevatorInMeters) {
                person.setState(PersonState.GONE_OVER);
                LOGGER.info(getLoggerPrefix(person, simulationTime) + "átment másik lakásba.");
            }
        } else {
            person.decrementSatisfaction(50);
            person.setWalkStartTime(new Time(simulationTime));
            person.setState(PersonState.ARRIVING_HOME_HEADING_TOWARDS_THE_ELEVATOR);
            LOGGER.info(getLoggerPrefix(person, simulationTime) + "elkésett, visszafordul.");
        }
    }

}