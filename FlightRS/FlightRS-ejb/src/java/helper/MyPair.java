package helper;

import entity.FlightSchedule;

public class MyPair {

    private FlightSchedule fs1;
    private FlightSchedule fs2;

    public MyPair() {
    }

    public MyPair(FlightSchedule fs1, FlightSchedule fs2) {
        this.fs1 = fs1;
        this.fs2 = fs2;
    }

    public FlightSchedule getFs1() {
        return fs1;
    }

    public void setFs1(FlightSchedule fs1) {
        this.fs1 = fs1;
    }

    public FlightSchedule getFs2() {
        return fs2;
    }

    public void setFs2(FlightSchedule fs2) {
        this.fs2 = fs2;
    }
}
