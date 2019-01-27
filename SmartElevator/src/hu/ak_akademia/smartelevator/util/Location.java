package hu.ak_akademia.smartelevator.util;

import java.util.Objects;

public class Location implements Comparable<Location> {

    private int floor;
    private int distanceFromTheElevatorInMeters;
    private int heightAboveGroundFloorInMeters;

    public Location(int floor, int distanceFromTheElevatorInMeters, int heightAboveGroundFloorInMeters) {
        this.floor = floor;
        this.distanceFromTheElevatorInMeters = distanceFromTheElevatorInMeters;
        this.heightAboveGroundFloorInMeters = heightAboveGroundFloorInMeters;
    }

    public Location(Location other) {
        this(other.floor, other.distanceFromTheElevatorInMeters, other.heightAboveGroundFloorInMeters);
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getDistanceFromTheElevatorInMeters() {
        return distanceFromTheElevatorInMeters;
    }

    public void setDistanceFromTheElevatorInMeters(int distanceFromTheElevatorInMeters) {
        this.distanceFromTheElevatorInMeters = distanceFromTheElevatorInMeters;
    }

    public int getHeightAboveGroundFloorInMeters() {
        return heightAboveGroundFloorInMeters;
    }

    public void setHeightAboveGroundFloorInMeters(int heightAboveGroundFloorInMeters) {
        this.heightAboveGroundFloorInMeters = heightAboveGroundFloorInMeters;
    }

    public boolean isAbove(Location other) {
        return this.compareTo(other) > 0;
    }

    public boolean isBelow(Location other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public int compareTo(Location other) {
        return Integer.compare(this.floor, other.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distanceFromTheElevatorInMeters, floor, heightAboveGroundFloorInMeters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        return distanceFromTheElevatorInMeters == other.distanceFromTheElevatorInMeters && floor == other.floor && heightAboveGroundFloorInMeters == other.heightAboveGroundFloorInMeters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Location [floor=");
        builder.append(floor);
        builder.append(", distanceFromTheElevatorInMeters=");
        builder.append(distanceFromTheElevatorInMeters);
        builder.append(", heightAboveGroundFloorInMeters=");
        builder.append(heightAboveGroundFloorInMeters);
        builder.append("]");
        return builder.toString();
    }

}