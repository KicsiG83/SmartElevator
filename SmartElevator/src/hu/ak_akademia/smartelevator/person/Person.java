package hu.ak_akademia.smartelevator.person;

import java.util.List;
import java.util.Optional;

import hu.ak_akademia.smartelevator.Simulatable;
import hu.ak_akademia.smartelevator.elevator.Elevator;
import hu.ak_akademia.smartelevator.house.Flat;
import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Location;
import hu.ak_akademia.smartelevator.util.Scale;
import hu.ak_akademia.smartelevator.util.Time;

public interface Person extends Simulatable {

    Name getName();

    int getAgeInYears();

    int getWeightInKilograms();

    Occupation getOccupation();

    List<AbsenceTime> getAbsenceTimes();

    Scale getSpeedInKilometersPerHour();

    Flat getResidency();

    void setResidency(Flat residency);

    Location getLocation();

    void setLocation(Location location);

    Scale getPatience();

    int incrementPatience();

    int incrementPatience(int amount);

    int decrementPatience();

    int decrementPatience(int amount);

    PersonState getState();

    void setState(PersonState state);

    Time getWalkStartTime();

    void setWalkStartTime(Time walkStartTime);

    int incrementSuccessfulElevatorRides();

    int getSuccessfulElevatorRides();

    int incrementFailedElevatorRides();

    int getFailedElevatorRides();

    int incrementWaitingTime();

    int getWaitingTimeInSeconds();

    int decrementSatisfaction();

    int decrementSatisfaction(int amount);

    int incrementSatisfaction();

    int incrementSatisfaction(int amount);

    int getSatisfaction();

    boolean isDepartureTime(Time time);

    boolean isArrivalTime(Time time);

    Elevator getTravellingWithElevator();

    void setTravellingWithElevator(Elevator elevator);

    Optional<AbsenceTime> getCurrentAbsenceTime(Time simulationTime);

}