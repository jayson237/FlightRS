/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author jayso
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date departureDateTime;

    @Column(nullable = false)
    @Min(0)
    @Max(24)
    private double estimatedDuration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date arrivalDateTime;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<SeatInventory> seatInventory;

    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.LAZY)
    private List<FlightReservation> flightReservations;

    public FlightSchedule() {
        this.seatInventory = new ArrayList<>();
        this.flightReservations = new ArrayList<>();
    }

    public FlightSchedule(Date departureDateTime, double estimatedDuration, Date arrivalDateTime) {
        this();
        this.departureDateTime = departureDateTime;
        this.estimatedDuration = estimatedDuration;
        this.arrivalDateTime = arrivalDateTime;
    }

    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public List<SeatInventory> getSeatInventory() {
        return seatInventory;
    }

    public void setSeatInventory(List<SeatInventory> seatInventory) {
        this.seatInventory = seatInventory;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleId + " ]";
    }

    public static class FlightScheduleComparator implements Comparator<FlightSchedule> {

        @Override
        public int compare(FlightSchedule o1, FlightSchedule o2) {
            if (o1.getDepartureDateTime().compareTo(o2.getDepartureDateTime()) > 0) {
                return 1;
            } else if (o1.getDepartureDateTime().compareTo(o2.getDepartureDateTime()) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static class ConnectingFlightScheduleComparator implements Comparator<Pair<FlightSchedule, FlightSchedule>> {

        @Override
        public int compare(Pair<FlightSchedule, FlightSchedule> p1, Pair<FlightSchedule, FlightSchedule> p2) {
            if (p1.getKey().getDepartureDateTime().compareTo(p2.getKey().getDepartureDateTime()) > 0) {
                return 1;
            } else if (p1.getKey().getDepartureDateTime().compareTo(p2.getKey().getDepartureDateTime()) < 0) {
                return -1;
            } else {
                return 0;
            }
        }

    }

}
