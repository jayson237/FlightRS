/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.FlightScheduleType;

/**
 *
 * @author timothy
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    @Enumerated(EnumType.STRING)
    private FlightScheduleType type;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date recurrentEndDate;
    @Column(nullable = false)
    private boolean complementaryReturnFlight;
    @Column(nullable = false)
    private Integer layOverDuration;
    @OneToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightSchedule> flightSchedules;

    public FlightSchedulePlan() {
    }

    public FlightSchedulePlan(FlightScheduleType type, Date recurrentEndDate, boolean complementaryReturnFlight, Integer layOverDuration) {
        this.type = type;
        this.recurrentEndDate = recurrentEndDate;
        this.complementaryReturnFlight = complementaryReturnFlight;
        this.layOverDuration = layOverDuration;
    }
    
    

    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
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

    /**
     * @return the type
     */
    public FlightScheduleType getFlightSchedule() {
        return type;
    }

    /**
     * @param flightSchedule the type to set
     */
    public void setFlightSchedule(FlightScheduleType flightSchedule) {
        this.type = flightSchedule;
    }

    /**
     * @return the recurrentEndDate
     */
    public Date getRecurrentEndDate() {
        return recurrentEndDate;
    }

    /**
     * @param recurrentEndDate the recurrentEndDate to set
     */
    public void setRecurrentEndDate(Date recurrentEndDate) {
        this.recurrentEndDate = recurrentEndDate;
    }

    /**
     * @return the complementaryReturnFlight
     */
    public boolean isComplementaryReturnFlight() {
        return complementaryReturnFlight;
    }

    /**
     * @param complementaryReturnFlight the complementaryReturnFlight to set
     */
    public void setComplementaryReturnFlight(boolean complementaryReturnFlight) {
        this.complementaryReturnFlight = complementaryReturnFlight;
    }

    /**
     * @return the layOverDuration
     */
    public Integer getLayOverDuration() {
        return layOverDuration;
    }

    /**
     * @param layOverDuration the layOverDuration to set
     */
    public void setLayOverDuration(Integer layOverDuration) {
        this.layOverDuration = layOverDuration;
    }

    /**
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * @param flight the flight to set
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * @return the flightSchedules
     */
    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    /**
     * @param flightSchedules the flightSchedules to set
     */
    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }
    
}
