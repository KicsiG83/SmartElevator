package hu.ak_akademia.smartelevator.util;

public class Destination {

    private final DestinationType type;
    private final Location location;

    public Destination() {
        type = DestinationType.LEAVE_HOUSE;
        location = null;
    }

    public Destination(DestinationType type, Location location) {
        this.type = type;
        this.location = location;
    }

    public DestinationType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return DestinationType.LEAVE_HOUSE.equals(type) ? "elhagyja a házat" : "átmegy ide: " + location;
    }

}