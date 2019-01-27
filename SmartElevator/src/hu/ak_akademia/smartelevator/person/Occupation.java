package hu.ak_akademia.smartelevator.person;

import java.util.List;

import hu.ak_akademia.smartelevator.util.AbsenceTime;
import hu.ak_akademia.smartelevator.util.Interval;
import hu.ak_akademia.smartelevator.util.Time;

public enum Occupation {

    STUDENT("tanuló", List.of(new AbsenceTime(new Time(7, 0, 0), new Time(14, 0, 0)), new AbsenceTime(new Time(17, 0, 0), new Time(20, 0, 0))), new Interval(2, 10)),
    EMPLOYEE("alkalmazott", List.of(new AbsenceTime(new Time(8, 0, 0), new Time(17, 0, 0)), new AbsenceTime(new Time(20, 0, 0), new Time(23, 0, 0))), new Interval(5, 12)),
    ENTREPRENEUR("vállalkozó", List.of(new AbsenceTime(new Time(9, 0, 0), new Time(21, 0, 0))), new Interval(7, 10)),
    PENSIONER("nyugdíjas", List.of(new AbsenceTime(new Time(10, 0, 0), new Time(12, 0, 0)), new AbsenceTime(new Time(15, 0, 0), new Time(17, 0, 0))), new Interval(1, 2));

    private final String name;
    private final List<AbsenceTime> typicalAbsenceTimes;
    private final Interval speedLimits;

    private Occupation(String name, List<AbsenceTime> typicalAbsenceTimes, Interval speedLimits) {
        this.name = name;
        this.typicalAbsenceTimes = typicalAbsenceTimes;
        this.speedLimits = speedLimits;
    }

    public String getName() {
        return name;
    }

    public List<AbsenceTime> getTypicalAbsenceTimes() {
        return typicalAbsenceTimes;
    }

    public Interval getSpeedLimits() {
        return speedLimits;
    }

}