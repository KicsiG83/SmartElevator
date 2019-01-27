package hu.ak_akademia.smartelevator.elevator.controller;

import java.util.Objects;

public class ElevatorCaller {

    private final int floor;
    private CallerState state = CallerState.CLEARED;

    public ElevatorCaller(int floor) {
        this.floor = floor;
    }

    public void signalUp() {
        switch (state) {
        case CLEARED:
        case SIGNALLED_UP:
            state = CallerState.SIGNALLED_UP;
            return;
        case SIGNALLED_DOWN:
        case SIGNALLED_BOTH_WAYS:
            state = CallerState.SIGNALLED_BOTH_WAYS;
            return;
        }
    }

    public void signalDown() {
        switch (state) {
        case CLEARED:
        case SIGNALLED_DOWN:
            state = CallerState.SIGNALLED_DOWN;
            return;
        case SIGNALLED_UP:
        case SIGNALLED_BOTH_WAYS:
            state = CallerState.SIGNALLED_BOTH_WAYS;
            return;
        }
    }

    public boolean isSignalledInEitherWay() {
        switch (state) {
        default:
        case CLEARED:
            return false;
        case SIGNALLED_DOWN:
        case SIGNALLED_UP:
        case SIGNALLED_BOTH_WAYS:
            return true;
        }
    }

    public boolean isSignalledUp() {
        switch (state) {
        default:
        case CLEARED:
        case SIGNALLED_DOWN:
            return false;
        case SIGNALLED_UP:
        case SIGNALLED_BOTH_WAYS:
            return true;
        }
    }

    public boolean isSignalledDown() {
        switch (state) {
        default:
        case CLEARED:
        case SIGNALLED_UP:
            return false;
        case SIGNALLED_DOWN:
        case SIGNALLED_BOTH_WAYS:
            return true;
        }
    }

    public boolean isSignalledBothWays() {
        switch (state) {
        default:
        case CLEARED:
        case SIGNALLED_UP:
        case SIGNALLED_DOWN:
            return false;
        case SIGNALLED_BOTH_WAYS:
            return true;
        }
    }

    public boolean isCleared() {
        switch (state) {
        default:
        case SIGNALLED_UP:
        case SIGNALLED_DOWN:
        case SIGNALLED_BOTH_WAYS:
            return false;
        case CLEARED:
            return true;
        }
    }

    public void clear() {
        state = CallerState.CLEARED;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ElevatorCaller)) {
            return false;
        }
        ElevatorCaller other = (ElevatorCaller) obj;
        return floor == other.floor;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ElevatorCaller [floor=");
        builder.append(floor);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }

}