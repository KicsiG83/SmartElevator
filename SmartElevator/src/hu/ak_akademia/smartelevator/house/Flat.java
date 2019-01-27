package hu.ak_akademia.smartelevator.house;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import hu.ak_akademia.smartelevator.person.Person;
import hu.ak_akademia.smartelevator.util.Location;

public class Flat {

    private final BlockOfFlats blockOfFlats;
    private final int doorNumber;
    private final Location location;
    private final Set<Person> residents = new HashSet<>();

    public Flat(BlockOfFlats blockOfFlats, int doorNumber, Location location) {
        this.blockOfFlats = blockOfFlats;
        this.doorNumber = doorNumber;
        this.location = location;
    }

    public BlockOfFlats getBlockOfFlats() {
        return blockOfFlats;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public Location getLocation() {
        return new Location(location);
    }

    public Set<Person> getResidents() {
        return residents;
    }

    public void moveIn(Person person) {
        person.setResidency(this);
        person.setLocation(getLocation());
        residents.add(person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockOfFlats, doorNumber, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Flat)) {
            return false;
        }
        Flat other = (Flat) obj;
        return Objects.equals(blockOfFlats, other.blockOfFlats) && doorNumber == other.doorNumber && Objects.equals(location, other.location);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nFlat [doorNumber=");
        builder.append(doorNumber);
        builder.append(", location=");
        builder.append(location);
        builder.append(", residents=");
        builder.append(residents);
        builder.append("]");
        return builder.toString();
    }

}