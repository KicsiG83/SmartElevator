package hu.ak_akademia.smartelevator.person;

import java.util.List;

import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Scale;

public class YoungAdult extends Resident {

    public YoungAdult(Name name, int ageInYears, int weightInKilograms, Occupation occupation, Scale speedInKilometersPerHour, List<AbsenceTime> absenceTimes, Scale patience) {
        super(name, ageInYears, weightInKilograms, occupation, speedInKilometersPerHour, absenceTimes, patience);
    }

}