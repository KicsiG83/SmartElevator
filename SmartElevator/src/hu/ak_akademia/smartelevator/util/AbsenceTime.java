package hu.ak_akademia.smartelevator.util;

import java.util.Optional;

public class AbsenceTime {

    private final Destination destination;
    private final Time departure;
    private final Time arrival;

    public AbsenceTime(Time departure, Time arrival) {
        this.destination = null;
        this.departure = departure;
        this.arrival = arrival;
    }

    public AbsenceTime(Destination destination, Time departure, Time arrival) {
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
    }

    public AbsenceTime(AbsenceTime absenceTime) {
        this(absenceTime.destination, new Time(absenceTime.departure), new Time(absenceTime.arrival));
    }

    public Optional<Destination> getDestination() {
        return Optional.ofNullable(destination);
    }

    public Time getDeparture() {
        return departure;
    }

    public Time getArrival() {
        return arrival;
    }

    public boolean isWithin(Time time) {
        return !time.isBefore(departure) && !time.isAfter(arrival);
    }

    @Override
    public String toString() {
        return departure + " - " + arrival + (destination == null ? "" : " (" + destination + ")");
    }

}