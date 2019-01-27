package hu.ak_akademia.smartelevator.person;

import hu.ak_akademia.smartelevator.person.activity.ArrivingHomeHeadingTowardsTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.ArrivingHomeHeadingTowardsTheFlatAction;
import hu.ak_akademia.smartelevator.person.activity.ArrivingHomeInTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.ArrivingHomeViaStairsAction;
import hu.ak_akademia.smartelevator.person.activity.ArrivingHomeWaitingForTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.BeingAwayFromHomeAction;
import hu.ak_akademia.smartelevator.person.activity.BeingHomeAction;
import hu.ak_akademia.smartelevator.person.activity.GoneOverAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeHeadTowardsTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeHeadingTowardsTheOtherFlatAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeInTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeInTheElevatorGoingOverAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeLeavingBuildingAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeViaStairsAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeViaStairsGoingOverAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeWaitingForTheElevatorAction;
import hu.ak_akademia.smartelevator.person.activity.LeavingHomeWaitingForTheElevatorGoingOverAction;

public enum PersonState {

    AT_HOME(new BeingHomeAction()),
    LEAVING_HOME_HEADING_TOWARDS_THE_ELEVATOR(new LeavingHomeHeadTowardsTheElevatorAction()),
    LEAVING_HOME_WAITING_FOR_THE_ELEVATOR_TO_GO_UP_SOME(new LeavingHomeWaitingForTheElevatorGoingOverAction()),
    LEAVING_HOME_WAITING_FOR_THE_ELEVATOR_TO_GO_DOWN_SOME(new LeavingHomeWaitingForTheElevatorGoingOverAction()),
    LEAVING_HOME_WAITING_FOR_THE_ELEVATOR(new LeavingHomeWaitingForTheElevatorAction()),
    LEAVING_HOME_IN_THE_ELEVATOR(new LeavingHomeInTheElevatorAction()),
    LEAVING_HOME_VIA_STAIRS(new LeavingHomeViaStairsAction()),
    LEAVING_HOME_LEAVING_BUILDING(new LeavingHomeLeavingBuildingAction()),
    LEAVING_HOME_IN_THE_ELEVATOR_GOING_OVER(new LeavingHomeInTheElevatorGoingOverAction()),
    LEAVING_HOME_VIA_STAIRS_GOING_OVER(new LeavingHomeViaStairsGoingOverAction()),
    LEAVING_HOME_HEADING_TOWARDS_THE_OTHER_FLAT(new LeavingHomeHeadingTowardsTheOtherFlatAction()),
    AWAY(new BeingAwayFromHomeAction()),
    GONE_OVER(new GoneOverAction()),
    ARRIVING_HOME_HEADING_TOWARDS_THE_ELEVATOR(new ArrivingHomeHeadingTowardsTheElevatorAction()),
    ARRIVING_HOME_WAITING_FOR_THE_ELEVATOR(new ArrivingHomeWaitingForTheElevatorAction()),
    ARRIVING_HOME_IN_THE_ELEVATOR(new ArrivingHomeInTheElevatorAction()),
    ARRIVING_HOME_VIA_STAIRS(new ArrivingHomeViaStairsAction()),
    ARRIVING_HOME_HEADING_TOWARDS_FLAT(new ArrivingHomeHeadingTowardsTheFlatAction());

    private final PersonActivity action;

    private PersonState(PersonActivity action) {
        this.action = action;
    }

    public PersonActivity getAction() {
        return action;
    }

}