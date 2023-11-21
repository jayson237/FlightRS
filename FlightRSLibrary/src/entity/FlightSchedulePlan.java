/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.FlightScheduleType;

/**
 *
 * @author jayso
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;

    @Column(nullable = false, length = 32)
    @Size(min = 5, max = 32)
    @NotNull
    private String flightNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private FlightScheduleType type;

    @Temporal(TemporalType.DATE)
    private Date recurrentEndDate;

    @Column(nullable = false)
    @NotNull
    private boolean isDisabled;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private Flight flight;

    @OneToMany(mappedBy = "flightSchedulePlan", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<FlightSchedule> flightSchedules;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private List<Fare> fares;

    public FlightSchedulePlan() {
        this.flightSchedules = new ArrayList<>();
        this.fares = new ArrayList<>();
    }

    public FlightSchedulePlan(String flightNumber, FlightScheduleType type, boolean isDisabled) {
        this();
        this.flightNumber = flightNumber;
        this.type = type;
        this.recurrentEndDate = null;
        this.isDisabled = isDisabled;
    }

    public FlightSchedulePlan(String flightNumber, FlightScheduleType type, Date recurrentEndDate, boolean isDisabled) {
        this();
        this.flightNumber = flightNumber;
        this.type = type;
        this.recurrentEndDate = recurrentEndDate;
        this.isDisabled = isDisabled;
    }

    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public FlightScheduleType getType() {
        return type;
    }

    public void setType(FlightScheduleType type) {
        this.type = type;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Date getRecurrentEndDate() {
        return recurrentEndDate;
    }

    public void setRecurrentEndDate(Date recurrentEndDate) {
        this.recurrentEndDate = recurrentEndDate;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanId + " ]";
    }

}
